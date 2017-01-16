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

    private TwitterLoginButton loginButton;
    Realm realm;
    ApiManager mApiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();
        mApiManager = new ApiManager(this);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                realm.beginTransaction();
                UserSession userSession = realm.createObject(UserSession.class);
                userSession.setUserName(session.getUserName());
                userSession.setUserId(session.getUserId());
                userSession.setAuthToken(session.getAuthToken().token);
                userSession.setAuthSecret(session.getAuthToken().secret);
                realm.commitTransaction();

                mApiManager.getBearerToken();
                startActivity(new Intent(LoginActivity.this,FollowersActivity.class));
                finish();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
