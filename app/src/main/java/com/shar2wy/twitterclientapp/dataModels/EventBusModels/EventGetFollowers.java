package com.shar2wy.twitterclientapp.dataModels.EventBusModels;

/**
 * Created by Shar2wy on 16/01/17.
 */

public class EventGetFollowers {

    String next_cursor_str;
    String previous_cursor_str;

    boolean success;

    public EventGetFollowers(boolean success,String next_cursor_str, String previous_cursor_str) {
        this.next_cursor_str = next_cursor_str;
        this.previous_cursor_str = previous_cursor_str;
        this.success = success;
    }

    public String getNext_cursor_str() {
        return next_cursor_str;
    }

    public void setNext_cursor_str(String next_cursor_str) {
        this.next_cursor_str = next_cursor_str;
    }

    public String getPrevious_cursor_str() {
        return previous_cursor_str;
    }

    public void setPrevious_cursor_str(String previous_cursor_str) {
        this.previous_cursor_str = previous_cursor_str;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
