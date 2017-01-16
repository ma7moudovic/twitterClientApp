package com.shar2wy.twitterclientapp.utilities;

/**
 * Created by Shar2wy on 16/01/17.
 */

public class UrlHelper {

    public static final String BASE_URL = "https://api.twitter.com/";
    public static final String URL_GET_FOLLOWERS = BASE_URL+"/1.1/followers/list.json";
    public static final String URL_GET_FOLLOWER = BASE_URL+"/1.1/users/show.json";
    public static final String URL_GET_TWEETS = BASE_URL+"1.1/statuses/user_timeline.json?include_rts=false";
    public static final String URL_GET_AUTH_TOKEN = BASE_URL+"oauth2/token";

}
