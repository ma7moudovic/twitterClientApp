package com.shar2wy.twitterclientapp.dataModels.EventBusModels;

/**
 * Created by Shar2wy on 17/01/17.
 */

public class EventGetBearToken {
    private boolean success;

    public EventGetBearToken(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
