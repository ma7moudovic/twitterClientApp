package com.shar2wy.twitterclientapp;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.shar2wy.twitterclientapp.utilities.Migration;
import com.shar2wy.twitterclientapp.utilities.RealmConfig;
import com.shar2wy.twitterclientapp.utilities.TwitterCredentials;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Shar2wy on 12/01/17.
 */

public class App extends Application {

    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        initFabricWithTwitter();
        initRealm();
        mInstance = this;
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private void initFabricWithTwitter() {
        Fabric.with(this, new Twitter(TwitterCredentials.getTwitterCredentials()));
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session != null) {
            Toast.makeText(this, "session : "+session.getUserName(), Toast.LENGTH_SHORT).show();
//            TwitterApiHelper.addCustomApiClients(session);
        }
    }

    public static synchronized Application getInstance() {
        return mInstance;
    }

}
