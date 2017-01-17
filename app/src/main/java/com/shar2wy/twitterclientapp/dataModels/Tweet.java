package com.shar2wy.twitterclientapp.dataModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Shar2wy on 12/01/17.
 */

public class Tweet extends RealmObject{

    public Integer favorite_count;

    public boolean favorited;

    public String filter_level;

    @PrimaryKey
    public long id;

    public String id_str;

    public String in_reply_to_screen_name;

    public String in_reply_to_status_id;

    public String in_reply_to_status_id_str;

    public String in_reply_to_user_id_str;

    public String lang;

    public String quoted_status_id;

    public String quoted_status_id_str;

    public Tweet quoted_status;

    public int retweet_count;

    public boolean retweeted;

    public Tweet retweeted_status;

    public String source;

    public String text;

    public boolean truncated;

    public Tweet() {
    }

    public Tweet(Integer favorite_count, boolean favorited, String filter_level, long id, String id_str, String in_reply_to_screen_name, String in_reply_to_status_id, String in_reply_to_status_id_str, long in_reply_to_user_id, String in_reply_to_user_id_str, String lang, String quoted_status_id, String quoted_status_id_str, Tweet quoted_status, int retweet_count, boolean retweeted, Tweet retweeted_status, String source, String text, boolean truncated) {
        this.favorite_count = favorite_count;
        this.favorited = favorited;
        this.filter_level = filter_level;
        this.id = id;
        this.id_str = id_str;
        this.in_reply_to_screen_name = in_reply_to_screen_name;
        this.in_reply_to_status_id = in_reply_to_status_id;
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
        this.in_reply_to_user_id_str = in_reply_to_user_id_str;
        this.lang = lang;
        this.quoted_status_id = quoted_status_id;
        this.quoted_status_id_str = quoted_status_id_str;
        this.quoted_status = quoted_status;
        this.retweet_count = retweet_count;
        this.retweeted = retweeted;
        this.retweeted_status = retweeted_status;
        this.source = source;
        this.text = text;
        this.truncated = truncated;
    }

    public Integer getFavorite_count() {
        return favorite_count;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public String getFilter_level() {
        return filter_level;
    }

    public long getId() {
        return id;
    }

    public String getId_str() {
        return id_str;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public String getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public String getIn_reply_to_status_id_str() {
        return in_reply_to_status_id_str;
    }

    public String getIn_reply_to_user_id_str() {
        return in_reply_to_user_id_str;
    }

    public String getLang() {
        return lang;
    }

    public String getQuoted_status_id() {
        return quoted_status_id;
    }

    public String getQuoted_status_id_str() {
        return quoted_status_id_str;
    }

    public Tweet getQuoted_status() {
        return quoted_status;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public Tweet getRetweeted_status() {
        return retweeted_status;
    }

    public String getSource() {
        return source;
    }

    public String getText() {
        return text;
    }

    public boolean isTruncated() {
        return truncated;
    }
}
