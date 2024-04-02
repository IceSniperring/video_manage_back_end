package com.videomanage.video_manage_after.status;

public class DeleteStatus {
    private boolean isSuccess;
    private int code;

    //code=1表示删除成功
    //code=2表示视频删除失败
    //code=3表示封面删除失败
    //code=4表示两者都删除失败
    //code=5表示数据库删除失败，即稿件不存在
    //code=6表示未知错误
    public DeleteStatus(boolean isSuccess, int code) {
        this.isSuccess = isSuccess;
        this.code = code;
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
}
