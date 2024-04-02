package com.videomanage.video_manage_after.status;

public class LoginStatus {
    private boolean isSuccess;
    private int code;
//    code=1表示登陆成功
//    code=2表示密码错误
//    code=3表示用户不存在
//    code=4表示未知错误


    public LoginStatus(boolean isSuccess, int code) {
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
