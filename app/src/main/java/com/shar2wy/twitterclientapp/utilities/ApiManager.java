package com.shar2wy.twitterclientapp.utilities;

import android.content.Context;
import android.provider.SyncStateContract;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.shar2wy.twitterclientapp.dataModels.EventBusModels.EventGetFollowers;

import org.greenrobot.eventbus.EventBus;
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
import static com.shar2wy.twitterclientapp.utilities.UrlHelper.URL_GET_FOLLOWERS;

/**
 * Created by Shar2wy on 16/01/17.
 */

public class ApiManager {

    private Context context;

    public ApiManager(Context context) {
        this.context = context;
    }

    public void getBearerToken(){
        JSONObject params = new JSONObject();
        try {
            params.put("grant_type","client_credentials");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonRequest jsonRequest = new JsonObjectRequest(POST,URL_GET_AUTH_TOKEN+"?grant_type=client_credentials", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                Toast.makeText(context,response.toString(), Toast.LENGTH_SHORT).show();
                RealmHelper.getInstance(context).saveBearerToken(Realm.getDefaultInstance(),response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Error", error.toString());
                Toast.makeText(context,error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String cosumerKeySecret = null;
                try {
                    cosumerKeySecret = URLEncoder.encode(TwitterCredentials.getTwitterCredentials().getConsumerKey(), "UTF-8") +":" +
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

    public void getFollowers(final String bearerToken, long userID, String screenName, String nextCursor, String count, String skip_status, String include_user_entities){

        if(bearerToken==null){
            getBearerToken();
        }
        String paramsInUrl ="?"+
        "user_id="+userID+
        "&screen_name="+screenName+
        "&cursor="+nextCursor+
        "&count="+count+
        "&skip_status="+skip_status+
        "&include_user_entities="+include_user_entities;

        Log.d("Url",URL_GET_AUTH_TOKEN+paramsInUrl);
        JsonRequest jsonRequest = new JsonObjectRequest(GET,URL_GET_FOLLOWERS+paramsInUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                Toast.makeText(context,response.toString(), Toast.LENGTH_SHORT).show();
                String previous_cursor_str = null;
                String next_cursor_str = null;
                try {
                    next_cursor_str = response.getString("next_cursor_str");
                    previous_cursor_str = response.getString("previous_cursor_str");
                    EventBus.getDefault().post(new EventGetFollowers(true,next_cursor_str,previous_cursor_str));

                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post(new EventGetFollowers(false,next_cursor_str,previous_cursor_str));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Error", error.toString());
                Toast.makeText(context,error.toString(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new EventGetFollowers(false,"",""));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String keyHeader = "Bearer "+bearerToken;
                headers.put("Authorization", keyHeader);
                headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//                headers.put("Content-Type", "application/form-data");
                Log.d("headers",headers.toString());
                return headers;
            }
        };

        addRequest(jsonRequest);
    }

    private void addRequest(JsonRequest jsonRequest){
        VolleyHelper.getInstance(context).addToRequestQueue(jsonRequest);
    }
}
