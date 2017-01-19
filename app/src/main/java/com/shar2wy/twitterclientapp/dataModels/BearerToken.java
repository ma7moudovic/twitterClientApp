package com.shar2wy.twitterclientapp.dataModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Shar2wy on 16/01/17.
 */

public class BearerToken extends RealmObject{

    @PrimaryKey
    private String access_token;

    private String token_type;

    public BearerToken() {
    }

    public BearerToken(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
}
