package com.shar2wy.twitterclientapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Shar2wy on 19/01/17.
 */

public class PrefsManager {

    private static PrefsManager sInstance;

    public static final String ENGLISH_LOCAL_LANGUAGE = "en";
    public static final String ARABIC_LOCAL_LANGUAGE = "ar";
    private static final String PREFS_FILE = "com.shar2wy.twitterclientapp.prefs.PREFS_FILE";
    private static final String PREF_SELECTED_LANGUAGE = "SELECTED_LANG";
    public static void init(Context context){
        sInstance = new PrefsManager(context);
    }

    public static PrefsManager getInstance() {
        if (sInstance == null) {
            throw new IllegalArgumentException(
                    "PrefsManager is not initialized! Call PrefsManager.init() first.");
        }
        return sInstance;
    }

    private SharedPreferences mPrefs;

    private PrefsManager(Context context) {
        mPrefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }

    public String getSelectedLanguage() {

        Log.d("lang pres get: ",mPrefs.getString(PREF_SELECTED_LANGUAGE, ENGLISH_LOCAL_LANGUAGE));
        return mPrefs.getString(PREF_SELECTED_LANGUAGE, ENGLISH_LOCAL_LANGUAGE);
    }

    public void setSelectedLanguage(String language) {

        Log.d("lang pres set: ",language);
        mPrefs.edit().putString(PREF_SELECTED_LANGUAGE, language).apply();
    }
}
