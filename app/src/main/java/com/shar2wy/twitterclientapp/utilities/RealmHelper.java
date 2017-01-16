package com.shar2wy.twitterclientapp.utilities;

import android.content.Context;

import com.shar2wy.twitterclientapp.dataModels.BearerToken;

import org.json.JSONObject;

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
    public void deleteBearerToken(Realm realm){
        realm.beginTransaction();
        realm.where(BearerToken.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }
    public String getBearerToken(Realm realm){
        return realm.where(BearerToken.class).findFirst().getAccess_token();
    }
}
