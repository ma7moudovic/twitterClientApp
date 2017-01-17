package com.shar2wy.twitterclientapp.dataModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Shar2wy on 12/01/17.
 */

public class Follower extends RealmObject{

    @PrimaryKey
    private long id;
    private String name;
    private String screen_name;
    private String description;
    private String profile_image_url;
    private String profile_banner_url;

    public Follower() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_banner_url() {
        return profile_banner_url;
    }

    public void setProfile_banner_url(String profile_banner_url) {
        this.profile_banner_url = profile_banner_url;
    }
}
