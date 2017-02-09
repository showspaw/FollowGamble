package com.heros.follow.Telegram.api.getupdates.imgur;

/**
 * Created by root on 2017/2/8.
 */
public class ImgurResult {
    private Data data;
    private boolean success;
    private int status;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
