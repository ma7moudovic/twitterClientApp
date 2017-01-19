package com.shar2wy.twitterclientapp.dataModels.EventBusModels;

/**
 * Created by Shar2wy on 16/01/17.
 */

public class EventGetTweets {
    private boolean success;

    public EventGetTweets(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
