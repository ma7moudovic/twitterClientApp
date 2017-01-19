package com.shar2wy.twitterclientapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.shar2wy.twitterclientapp.R;
import com.shar2wy.twitterclientapp.dataModels.UserSession;
import com.shar2wy.twitterclientapp.utilities.ApiManager;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {

    private TwitterLoginButton mLoginButton;
    private Realm mRealm;
    private ApiManager mApiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRealm = Realm.getDefaultInstance();
        mApiManager = new ApiManager(this);

        mLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;

                mRealm.beginTransaction();
                UserSession userSession = mRealm.createObject(UserSession.class);
                userSession.setUserName(session.getUserName());
                userSession.setUserId(session.getUserId());
                userSession.setAuthToken(session.getAuthToken().token);
                userSession.setAuthSecret(session.getAuthToken().secret);
                mRealm.commitTransaction();

                mApiManager.getBearerToken();
                startActivity(new Intent(LoginActivity.this,FollowersActivity.class));
                finish();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
                Toast.makeText(LoginActivity.this,getString(R.string.msg_fail_to_login), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the mLoginButton hears the result from any
        // Activity that it triggered.
        mLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!mRealm.isClosed()){
            mRealm.close();
        }
    }
}
