package com.shar2wy.twitterclientapp.dataModels.eventBus;

/**
 * Created by Shar2wy on 16/01/17.
 */

public class EventGetFollowers {

    private String mNextCursor;
    private String mPreviousCursor;

    private boolean mSuccess;

    public EventGetFollowers(boolean mSuccess, String mNextCursor, String mPreviousCursor) {
        this.mNextCursor = mNextCursor;
        this.mPreviousCursor = mPreviousCursor;
        this.mSuccess = mSuccess;
    }

    public String getmNextCursor() {
        return mNextCursor;
    }

    public void setmNextCursor(String mNextCursor) {
        this.mNextCursor = mNextCursor;
    }

    public String getmPreviousCursor() {
        return mPreviousCursor;
    }

    public void setmPreviousCursor(String mPreviousCursor) {
        this.mPreviousCursor = mPreviousCursor;
    }

    public boolean ismSuccess() {
        return mSuccess;
    }

    public void setmSuccess(boolean mSuccess) {
        this.mSuccess = mSuccess;
    }
}
