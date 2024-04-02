package com.videomanage.video_manage_after.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.videomanage.video_manage_after.utils.FileOperation;
import com.videomanage.video_manage_after.status.LoginStatus;
import com.videomanage.video_manage_after.entity.User;
import com.videomanage.video_manage_after.mapper.UserMapper;
import com.videomanage.video_manage_after.status.SignUpStatus;
import com.videomanage.video_manage_after.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;


@RestController
@CrossOrigin
public class UserController {
    private final UserMapper userMapper;

    @Value("${upload-video-dir}")
    private String uploadVideoDir;

    @Autowired
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/api/getUserInfo")
    public User getUserInfo(@RequestParam(value = "username") String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (username.contains("@"))
            queryWrapper.eq("email", username);
        else
            queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @PostMapping("/api/login")
    public LoginStatus login(@RequestBody User user) {
        try {
            String decryptPasword = RSAUtils.decryptByPrivateKey(user.getPassword());
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            if (user.getUsername().contains("@"))
                queryWrapper.eq("email", user.getUsername());
            else
                queryWrapper.eq("username", user.getUsername());
            User userTarget = userMapper.selectOne(queryWrapper);
            if (userTarget != null) {
                if (RSAUtils.decryptByPrivateKey(userTarget.getPassword()).equals(decryptPasword)) {
                    return new LoginStatus(true, 1);
                } else
                    return new LoginStatus(false, 2);
            } else
                return new LoginStatus(false, 3);
        } catch (Exception e) {
            System.out.printf(e.toString());
            return new LoginStatus(false, 4);
        }
    }

    //注册接口

    @PostMapping("/api/signup")
    public SignUpStatus SignUp(String username,
                               String email,
                               String password,
                               MultipartFile avatarFile) {
        User user = new User();
        QueryWrapper<User> queryWrapper_username = new QueryWrapper<>();
        queryWrapper_username.eq("username", username);
        if (!userMapper.selectList(queryWrapper_username).isEmpty()) {
            return new SignUpStatus(false, 2);
        }
        QueryWrapper<User> queryWrapper_email = new QueryWrapper<>();
        queryWrapper_email.eq("email", email);
        if (!userMapper.selectList(queryWrapper_email).isEmpty()) {
            return new SignUpStatus(false, 3);
        }
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        String fileType = FileOperation.getFileType(Objects.requireNonNull(avatarFile.getOriginalFilename()));
        user.setAvatarPath("/avatar/" + username + fileType);
        try {
            FileOperation.saveFile(uploadVideoDir + "/avatar/" + username + fileType, avatarFile);
        } catch (Exception e) {
            return new SignUpStatus(false, 4);
        }
        if (userMapper.insert(user) != 0)
            return new SignUpStatus(true, 1);
        else
            return new SignUpStatus(false, 4);
    }
}
