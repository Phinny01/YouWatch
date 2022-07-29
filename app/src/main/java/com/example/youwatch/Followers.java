package com.example.youwatch;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Followers")
public class Followers extends ParseObject {
    public static final String KEY_USER = "User";
    public static final String KEY_FOLLOWER = "Follower";

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getFollowers() {
        return getString(KEY_FOLLOWER);
    }

    public void setFollower(String follower, boolean status, Followers followers) {
        if (status) {
            put(KEY_FOLLOWER, followers.getFollowers() + " " + follower);
        } else {
            put(KEY_FOLLOWER, getFollowers().replace(follower, ""));
        }
    }
}
