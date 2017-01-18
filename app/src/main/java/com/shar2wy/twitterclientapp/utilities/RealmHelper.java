package com.shar2wy.twitterclientapp.utilities;

import android.content.Context;

import com.shar2wy.twitterclientapp.dataModels.BearerToken;
import com.shar2wy.twitterclientapp.dataModels.Follower;
import com.shar2wy.twitterclientapp.dataModels.Tweet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Shar2wy on 16/01/17.
 */

public class RealmHelper {
    private Context mContext;
    private static RealmHelper mInstance;
//    private Realm realm;


    public RealmHelper(Context mContext) {
        this.mContext = mContext;
    }

    public static RealmHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RealmHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public void saveBearerToken(Realm realm,JSONObject bearerTokenJsonObject){
        realm.beginTransaction();
        realm.createOrUpdateObjectFromJson(BearerToken.class,bearerTokenJsonObject);
        realm.commitTransaction();
    }

    public void saveFollowers(Realm realm,JSONArray followersArray){
        realm.beginTransaction();
        realm.createOrUpdateAllFromJson(Follower.class,followersArray);
        realm.commitTransaction();
    }

    public List<Follower> getFollowers(Realm realm){
        return realm.where(Follower.class).findAll();
    }

    public Follower getFollower(Realm realm,long followerId){
        return realm.where(Follower.class).equalTo("id",followerId).findFirst();
    }

    public void deleteAllFolowers(Realm realm){
        realm.beginTransaction();
        realm.where(Follower.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void deleteBearerToken(Realm realm){
        realm.beginTransaction();
        realm.where(BearerToken.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }
    public BearerToken getBearerToken(Realm realm){
        return realm.where(BearerToken.class).findFirst();
    }

    public void saveTweets(Realm realm, JSONArray tweetsArray) {
        realm.beginTransaction();
        realm.where(Tweet.class).findAll().deleteAllFromRealm();
        realm.createOrUpdateAllFromJson(Tweet.class,tweetsArray);
        realm.commitTransaction();
    }

    public Collection<? extends Tweet> getTweets(Realm realm) {
        return realm.where(Tweet.class).findAll();
    }

    public void saveUserInfo(Realm realm, JSONObject userJsonObject) {
        realm.beginTransaction();
        realm.createOrUpdateObjectFromJson(Follower.class,userJsonObject);
        realm.commitTransaction();
    }

    public Follower getUserInfo(Realm realm,long id){
        return realm.where(Follower.class).equalTo("id",id).findFirst();
    }
}
