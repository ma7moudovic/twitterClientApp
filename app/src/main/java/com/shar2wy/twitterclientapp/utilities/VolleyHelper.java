package com.shar2wy.twitterclientapp.utilities;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class VolleyHelper {
    private static final String TAG = VolleyHelper.class.getSimpleName();
    private static final int MAX_CACHE_SIZE = 1024 * 1024 * 10;
    private static VolleyHelper mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    private VolleyHelper(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley
                    .newRequestQueue(mContext.getApplicationContext(), MAX_CACHE_SIZE);
        }
        return mRequestQueue;
    }

    public void start() {
        mRequestQueue.start();
    }

    public void stop() {
        mRequestQueue.stop();
    }

    public <T> void addToRequestQueue(Request<T> request) {
        Log.d(TAG, "Add Request " + request.toString());
        getRequestQueue().add(request);

    }

    public void cancelRequests(String tag) {
        mRequestQueue.cancelAll(tag);
    }
}
