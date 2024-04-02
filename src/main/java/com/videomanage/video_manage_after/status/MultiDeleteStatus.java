package com.videomanage.video_manage_after.status;

//批量删除状态
public class MultiDeleteStatus {
    private DeleteStatus deleteStatus;
    private int failedId;//删除失败的视频的Id，一旦有一个失败，那么后面的都无法执行删除,-1代表没有删除失败
    public DeleteStatus getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(DeleteStatus deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public int getFailedId() {
        return failedId;
    }

    public void setFailedId(int failedId) {
        this.failedId = failedId;
    }

    public MultiDeleteStatus(DeleteStatus deleteStatus, int failedId) {
        this.deleteStatus = deleteStatus;
        this.failedId = failedId;
    }
}
