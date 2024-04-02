package com.videomanage.video_manage_after.status;

public class SignUpStatus {
    private boolean isSuccess;
    private int code;
//    code=1表示注册成功
//    code=2表示用户名存在
//    code=3表示邮箱存在
//    code=4表示未知错误


    public SignUpStatus(boolean isSuccess, int code) {
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
