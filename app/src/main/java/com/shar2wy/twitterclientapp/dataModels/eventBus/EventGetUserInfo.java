package com.shar2wy.twitterclientapp.dataModels.eventBus;

/**
 * Created by Shar2wy on 18/01/17.
 */
public class EventGetUserInfo {

    private boolean mSuccess;

    public EventGetUserInfo(boolean mSuccess) {
        this.mSuccess = mSuccess;
    }

    public boolean ismSuccess() {
        return mSuccess;
    }

    public void setmSuccess(boolean mSuccess) {
        this.mSuccess = mSuccess;
    }
}
