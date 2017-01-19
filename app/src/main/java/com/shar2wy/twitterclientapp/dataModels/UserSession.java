package com.shar2wy.twitterclientapp.dataModels;

import io.realm.RealmObject;

/**
 * Created by Shar2wy on 12/01/17.
 */

public class UserSession extends RealmObject{

    private long UserId;
    private String UserName;
    private String AuthToken;
    private String AuthSecret;

    public UserSession() {

    }

    public UserSession(long userId, String userName, String AuthToken, String AuthSecret) {
        UserId = userId;
        UserName = userName;
        this.AuthToken = AuthToken;
        this.AuthSecret = AuthSecret;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public String getAuthSecret() {
        return AuthSecret;
    }

    public void setAuthSecret(String authSecret) {
        AuthSecret = authSecret;
    }
}
