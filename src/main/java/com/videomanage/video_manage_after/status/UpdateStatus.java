package com.videomanage.video_manage_after.status;

public class UpdateStatus {
    private boolean isSuccess;
    private int code;
//    code=1表示修改成功
//    code=2修改失败的未知错误
//    code=3表示执行删除旧封面的操作失败
//    code=4表示执行保存新封面的操作失败
//    code=5表示无此视频

    public UpdateStatus(boolean isSuccess, int code) {
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
