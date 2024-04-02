package com.videomanage.video_manage_after;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@MapperScan("com.videomanage.video_manage_after.mapper")
public class VideoManageAfterApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoManageAfterApplication.class, args);
    }

}
