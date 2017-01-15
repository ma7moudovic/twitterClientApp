package com.shar2wy.twitterclientapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shar2wy.twitterclientapp.R;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

public class DispatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session != null) {
            startUserFollowersActivity();
        } else {
            startTwitterLoginActivity();
        }
        finish();
    }

    private void startUserFollowersActivity() {
        startActivity(new Intent(DispatchActivity.this,FollowersActivity.class));
    }

    private void startTwitterLoginActivity() {
        startActivity(new Intent(DispatchActivity.this,LoginActivity.class));
    }
}
