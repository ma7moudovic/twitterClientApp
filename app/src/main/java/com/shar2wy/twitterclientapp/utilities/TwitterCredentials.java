package com.shar2wy.twitterclientapp.utilities;

import com.twitter.sdk.android.core.TwitterAuthConfig;

/**
 * Created by mahmoud on 11/18/16.
 */

public final class TwitterCredentials {
    private static final String TWITTER_KEY = "jBKBmIFNk7udrXf8WOSKhoHfa";
    private static final String TWITTER_SECRET = "k7zE2iebcXbIwHuUWltO8qVTv7Fxhp3ZiDW7gWW27QC3kgxpXo";

    public static TwitterAuthConfig getTwitterCredentials() {
        return new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
    }
}
