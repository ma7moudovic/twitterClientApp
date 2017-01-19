package com.shar2wy.twitterclientapp;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;


/**
 * Created by Shar2wy on 19/01/17.
 */

public class LocaleHelper {

    public static String getSelectedLanguage() {

        if(PrefsManager.getInstance().getSelectedLanguage().equalsIgnoreCase(PrefsManager.ARABIC_LOCAL_LANGUAGE)){
            return PrefsManager.ARABIC_LOCAL_LANGUAGE;
        }else {
            return PrefsManager.ENGLISH_LOCAL_LANGUAGE;
        }

    }

    public static void setSelectedLanguage(String language, Context context) {
        PrefsManager.getInstance().setSelectedLanguage(language);
        Locale locale;

        locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }
}
