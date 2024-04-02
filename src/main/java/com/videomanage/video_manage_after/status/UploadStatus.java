package com.videomanage.video_manage_after.status;

import com.videomanage.video_manage_after.entity.Video;

public class UploadStatus {
    private boolean isSuccess;
    //code=1表示上传成功
    //code=2表示视频上传失败
    //code=3表示图片上传失败
    //code=4表示两者都上传失败
    //code=5表示未知错误
    private int code;


    //上传成功之后返回上传的视频的信息
    private Video video;

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UploadStatus(boolean isSuccess, int code, Video video) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.video = video;
    }
}
