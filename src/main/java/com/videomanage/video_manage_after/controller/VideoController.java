package com.videomanage.video_manage_after.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.videomanage.video_manage_after.entity.Video;
import com.videomanage.video_manage_after.mapper.VideoMapper;
import com.videomanage.video_manage_after.status.DeleteStatus;
import com.videomanage.video_manage_after.status.MultiDeleteStatus;
import com.videomanage.video_manage_after.status.UpdateStatus;
import com.videomanage.video_manage_after.status.UploadStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.videomanage.video_manage_after.utils.FileOperation;

import java.util.List;

@RestController
@CrossOrigin
public class VideoController {
    private final VideoMapper videoMapper;

    @Autowired
    public VideoController(VideoMapper videoMapper) {
        this.videoMapper = videoMapper;
    }

    @Value("${upload-video-dir}")
    private String uploadVideoDir;

    @GetMapping("/api/getRandomPost")
    public List<Video> getRandomPost() {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("limit 5");
        queryWrapper.orderByAsc("RAND()");
        return videoMapper.selectList(queryWrapper);
    }

    //获取随机视频
    @GetMapping("/api/getRandomVideo")
    public IPage<Video> getRandomVideo(@RequestParam(value = "page", defaultValue = "1") int currentPage) {
        Page<Video> page = new Page<>(currentPage, 20);
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("RAND()");
        return videoMapper.selectPage(page, queryWrapper);
    }

    @GetMapping("/api/getVideoById")
    public Video getVideoById(@RequestParam(value = "id") int id) {
        Video video = videoMapper.selectById(id);
        if (video != null) {
            return video;
        } else {
            Video video_null = new Video();
            video_null.setId(-1);
            return video_null;
        }
    }

    @GetMapping("/api/getRandom4Video")
    public List<Video> getRandom4Video() {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("limit 4");
        queryWrapper.orderByAsc("RAND()");
        return videoMapper.selectList(queryWrapper);
    }

    //获取分类
    @GetMapping("/api/getKind")
    public String[] getKind() {
        return videoMapper.getKind();
    }

    //按类型分页获取视频
    @GetMapping("/api/getVideoByKind")
    public IPage<Video> pagination(@RequestParam(value = "page", defaultValue = "1") int currentPage, @RequestParam String kind) {
        Page<Video> page = new Page<>(currentPage, 20);
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("kind", kind);
        return videoMapper.selectPage(page, queryWrapper);
    }

    //搜索视频
    @GetMapping("/api/searchVideo")
    public Page<Video> searchVideo(@RequestParam(value = "title") String title,
                                   @RequestParam(value = "page", defaultValue = "1") int currentPage) {
        Page<Video> page = new Page<>(currentPage, 20);
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        return videoMapper.selectPage(page, queryWrapper);
    }

    @GetMapping("/api/getVideoByUid")
    public List<Video> getVideoByUid(@RequestParam(value = "uid") int uid) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        return videoMapper.selectList(queryWrapper);
    }

    //上传视频的api
    @PostMapping("/api/videoUpload")
    public UploadStatus videoUpload(@RequestParam int uid,
                                    @RequestParam String title,
                                    MultipartFile videoFile,
                                    @RequestParam String kind,
                                    MultipartFile postFile) {
        LocalDateTime currentTime = LocalDateTime.now();
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化当前时间
        String formattedDateTime = currentTime.format(formatter);
        String filePath = "/" + kind + "/" + formattedDateTime.replaceAll("[^a-zA-Z0-9.-]", "_") + "/" + videoFile.getOriginalFilename();
        String postPath = "/" + kind + "/" + "image/" + formattedDateTime.replaceAll("[^a-zA-Z0-9.-]", "_") + "/" + postFile.getOriginalFilename();
        int code = 1;
        boolean isVideoUploadSuccess = true;
        boolean isPostUploadSuccess = true;
        try {
            if (!FileOperation.saveFile(uploadVideoDir + "/" + kind + "/" +
                    formattedDateTime.replaceAll("[^a-zA-Z0-9.-]", "_") +
                    "/" + videoFile.getOriginalFilename(), videoFile))
                isVideoUploadSuccess = false;
            if (!FileOperation.saveFile(uploadVideoDir + "/" + kind +
                    "/image/" + formattedDateTime.replaceAll("[^a-zA-Z0-9.-]", "_")
                    + "/" + postFile.getOriginalFilename(), postFile))
                isPostUploadSuccess = false;
        } catch (Exception e) {
            return new UploadStatus(false, 5, null);
        }
        //视频上传失败
        if (!isVideoUploadSuccess) code = 2;
        //封面上传失败
        if (!isPostUploadSuccess) code = 3;
        //两者都失败
        if (!isVideoUploadSuccess && !isPostUploadSuccess) code = 4;
        if (code != 1) {
            return new UploadStatus(false, code, null);
        } else {
            videoMapper.InsertTest(uid, title, formattedDateTime, kind, filePath, postPath);
            QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("upload_date", formattedDateTime);
            Video new_video = videoMapper.selectOne(queryWrapper);
            return new UploadStatus(true, 1, new_video);
        }
    }

    @PutMapping("/api/updateVideoInfo")
    public UpdateStatus updateVideoInfo(@RequestParam int id,
                                        @RequestParam String title,
                                        MultipartFile postFile) {
        Video oldVideoInfo = videoMapper.selectById(id);
        if (oldVideoInfo == null) {
            return new UpdateStatus(false, 5);
        } else {

            //获取需要更新视频的地址
            String newVideoPostPath = oldVideoInfo.getPostPath().substring(0, oldVideoInfo.getPostPath().lastIndexOf("/"))
                    + "/" + postFile.getOriginalFilename();
            UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            updateWrapper.set("title", title);
            updateWrapper.set("post_path", newVideoPostPath);
            int code = 1;
            try {
                //先保存封面，防止删除出现错误，导致前端不显示
                if (!FileOperation.saveFile(uploadVideoDir + newVideoPostPath, postFile)) {
                    code = 4;
                }
                if (!FileOperation.deleteFile(Paths.get(uploadVideoDir, oldVideoInfo.getPostPath()))) {
                    code = 3;
                }
            } catch (Exception e) {
                System.out.printf(e.toString());
                return new UpdateStatus(false, 2);
            }
            if (code == 1)
                if (videoMapper.update(updateWrapper) != 0)
                    return new UpdateStatus(true, 1);
                else
                    return new UpdateStatus(false, 2);
            else
                return new UpdateStatus(false, code);
        }
    }


    @DeleteMapping("/api/deleteVideo")
    public DeleteStatus deleteVideo(@RequestParam int id) {
        Video deleteVideo = videoMapper.selectById(id);
        if (videoMapper.deleteById(id) != 0) {
            //删除视频和图片
            Path filePath = Paths.get(uploadVideoDir, deleteVideo.getFilePath());
            Path postPath = Paths.get(uploadVideoDir, deleteVideo.getPostPath());
            int code = 1;
            boolean isVideoDeleteSuccess = true;
            boolean isPostDeleteSuccess = true;
            //视频删除
            try {
                if (!FileOperation.deleteFile(filePath)) {
                    isVideoDeleteSuccess = false;
                }
                //封面删除
                if (!FileOperation.deleteFile(postPath)) {
                    isPostDeleteSuccess = false;
                }
            } catch (Exception e) {
                System.out.printf(e.toString());
                return new DeleteStatus(false, 6);
            }
            if (!isVideoDeleteSuccess) code = 2;
            if (!isPostDeleteSuccess) code = 3;
            if (!isPostDeleteSuccess && isVideoDeleteSuccess) code = 4;
            if (code != 1) {
                return new DeleteStatus(false, code);
            } else
                return new DeleteStatus(true, 1);
        } else return new DeleteStatus(false, 5);//数据库不存在
    }

    @DeleteMapping("/api/deleteVideos")
    public MultiDeleteStatus deleteVideos(@RequestParam int[] idList) {
        //这里我选择一个一个删除，因为如果批量删除可能导致视频删除，而数据库未删除的情况
        for (int i : idList) {
            DeleteStatus deleteStatus = deleteVideo(i);
            if (!deleteStatus.isSuccess()) return new MultiDeleteStatus(deleteStatus, i);
        }
        //如果都是删除成功，那么直接返回删除成功
        return new MultiDeleteStatus(new DeleteStatus(true,1),-1);
    }
}
