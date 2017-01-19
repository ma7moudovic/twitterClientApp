package com.shar2wy.twitterclientapp.dataModels.eventBus;

/**
 * Created by Shar2wy on 17/01/17.
 */

public class EventGetBearToken {
    private boolean mSuccess;

    public EventGetBearToken(boolean mSuccess) {
        this.mSuccess = mSuccess;
    }

    public boolean ismSuccess() {
        return mSuccess;
    }

    public void setmSuccess(boolean mSuccess) {
        this.mSuccess = mSuccess;
    }
}
