package com.shar2wy.twitterclientapp.dataModels.EventBusModels;

/**
 * Created by Shar2wy on 18/01/17.
 */
public class EventGetUserInfo {

    boolean success;

    public EventGetUserInfo(boolean success) {
        this.success=success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
