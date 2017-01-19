package com.shar2wy.twitterclientapp;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import com.shar2wy.twitterclientapp.utilities.TwitterCredentials;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by Shar2wy on 12/01/17.
 */

public class App extends Application {

    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        initPrefs();
        initFabricWithTwitter();
        initRealm();
        mInstance = this;

        Log.d("lang helper get: ",LocaleHelper.getSelectedLanguage());

        LocaleHelper.setSelectedLanguage(LocaleHelper.getSelectedLanguage(),this);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleHelper.setSelectedLanguage(LocaleHelper.getSelectedLanguage(), this);
    }

    private void initPrefs() {
        PrefsManager.init(this);
    }

    public static synchronized Application getInstance() {
        return mInstance;
    }

}
