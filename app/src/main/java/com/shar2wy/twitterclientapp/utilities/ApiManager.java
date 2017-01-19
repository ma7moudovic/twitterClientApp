package com.shar2wy.twitterclientapp.utilities;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.shar2wy.twitterclientapp.dataModels.EventBusModels.EventGetBearToken;
import com.shar2wy.twitterclientapp.dataModels.EventBusModels.EventGetFollowers;
import com.shar2wy.twitterclientapp.dataModels.EventBusModels.EventGetTweets;
import com.shar2wy.twitterclientapp.dataModels.EventBusModels.EventGetUserInfo;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.shar2wy.twitterclientapp.utilities.UrlHelper.URL_GET_AUTH_TOKEN;
import static com.shar2wy.twitterclientapp.utilities.UrlHelper.URL_GET_USER_INFO;
import static com.shar2wy.twitterclientapp.utilities.UrlHelper.URL_GET_FOLLOWERS;
import static com.shar2wy.twitterclientapp.utilities.UrlHelper.URL_GET_TWEETS;

/**
 * Created by Shar2wy on 16/01/17.
 */

public class ApiManager {

    private Context context;

    public ApiManager(Context context) {
        this.context = context;
    }

    public void getBearerToken() {

        JsonRequest jsonRequest = new JsonObjectRequest(POST, URL_GET_AUTH_TOKEN + "?grant_type=client_credentials", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
//                Toast.makeText(context,response.toString(), Toast.LENGTH_SHORT).show();
                RealmHelper.getInstance(context).saveBearerToken(Realm.getDefaultInstance(), response);
                EventBus.getDefault().post(new EventGetBearToken(true));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Error", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new EventGetBearToken(false));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String cosumerKeySecret = null;
                try {
                    cosumerKeySecret = URLEncoder.encode(TwitterCredentials.getTwitterCredentials().getConsumerKey(), "UTF-8") + ":" +
                            URLEncoder.encode(TwitterCredentials.getTwitterCredentials().getConsumerSecret(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String encodedConsumerKey = Base64.encodeToString(cosumerKeySecret.getBytes(),
                        Base64.NO_WRAP);
                Log.d("encoded key", encodedConsumerKey);
                String keyHeader = "Basic " + encodedConsumerKey;
                Log.d("Header", keyHeader);
                headers.put("Authorization", keyHeader);
                headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//                headers.put("Content-Type", "application/form-data");

                return headers;
            }
        };

        addRequest(jsonRequest);
    }

    public void getFollowers(final String bearerToken, long userID, String nextCursor, boolean skip_status, boolean include_user_entities) {
        if (bearerToken == null) {
            getBearerToken();
        }
        String paramsInUrl = "?" +
                "user_id=" + userID +
                "&cursor=" + nextCursor +
//        "&count="+count+
                "&skip_status=" + skip_status +
                "&include_user_entities=" + include_user_entities;

        Log.d("Url", URL_GET_AUTH_TOKEN + paramsInUrl);
        JsonRequest jsonRequest = new JsonObjectRequest(GET, URL_GET_FOLLOWERS + paramsInUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
//                Toast.makeText(context,response.toString(), Toast.LENGTH_SHORT).show();
                String previous_cursor_str = null;
                String next_cursor_str = null;
                try {
                    next_cursor_str = response.getString("next_cursor_str");
                    previous_cursor_str = response.getString("previous_cursor_str");
                    RealmHelper.getInstance(context).saveFollowers(Realm.getDefaultInstance(), response.getJSONArray("users"));
                    EventBus.getDefault().post(new EventGetFollowers(true, next_cursor_str, previous_cursor_str));

                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post(new EventGetFollowers(false, next_cursor_str, previous_cursor_str));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Error", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new EventGetFollowers(false, "", ""));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String keyHeader = "Bearer " + bearerToken;
                headers.put("Authorization", keyHeader);
                headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//                headers.put("Content-Type", "application/form-data");
                Log.d("headers", headers.toString());
                return headers;
            }
        };

        addRequest(jsonRequest);
    }

    public void getUserInfo(final String bearerToken, long userID) {
        if (bearerToken == null) {
            getBearerToken();
        }
        String paramsInUrl = "?" +
                "user_id=" + userID;
        Log.d("url", URL_GET_USER_INFO + paramsInUrl);
        JsonRequest jsonRequest = new JsonObjectRequest(GET, URL_GET_USER_INFO + paramsInUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
//                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();

                RealmHelper.getInstance(context).saveUserInfo(Realm.getDefaultInstance(), response);
                EventBus.getDefault().post(new EventGetUserInfo(true));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Error", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new EventGetFollowers(false, "", ""));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String keyHeader = "Bearer " + bearerToken;
                headers.put("Authorization", keyHeader);
                headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//                headers.put("Content-Type", "application/form-data");
                Log.d("headers", headers.toString());
                return headers;
            }
        };

        addRequest(jsonRequest);
    }

    public void getTweets(final String bearerToken, long userID, int count, boolean include_user_entities) {
        if (bearerToken == null) {
            getBearerToken();
        }
        String paramsInUrl = "?" +
                "user_id=" + userID +
                "&count=" + count +
                "&include_rts=false";

        Log.d("url", URL_GET_TWEETS + paramsInUrl);
        JsonArrayRequest jsonRequest = new JsonArrayRequest(GET, URL_GET_TWEETS + paramsInUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response", response.toString());
//                Toast.makeText(context,response.toString(), Toast.LENGTH_SHORT).show();

                RealmHelper.getInstance(context).saveTweets(Realm.getDefaultInstance(), response);
                EventBus.getDefault().post(new EventGetTweets(true));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Error", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new EventGetTweets(false));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String keyHeader = "Bearer " + bearerToken;
                headers.put("Authorization", keyHeader);
                headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//                headers.put("Content-Type", "application/form-data");
                Log.d("headers", headers.toString());
                return headers;
            }
        };

        addRequest(jsonRequest);
    }

    private void addRequest(JsonRequest jsonRequest) {
        VolleyHelper.getInstance(context).addToRequestQueue(jsonRequest);
    }
}
