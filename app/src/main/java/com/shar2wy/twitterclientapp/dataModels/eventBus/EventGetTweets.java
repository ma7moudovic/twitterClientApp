package com.shar2wy.twitterclientapp.dataModels.eventBus;

/**
 * Created by Shar2wy on 16/01/17.
 */

public class EventGetTweets {
    private boolean mSuccess;

    public EventGetTweets(boolean mSuccess) {
        this.mSuccess = mSuccess;
    }

    public boolean ismSuccess() {
        return mSuccess;
    }

    public void setmSuccess(boolean mSuccess) {
        this.mSuccess = mSuccess;
    }
}
