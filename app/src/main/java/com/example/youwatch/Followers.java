package com.example.youwatch;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Followers")
public class Followers extends ParseObject {
    public static final String KEY_USER = "User";
    public static final String KEY_FOLLOWER = "Follower";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseUser getFollower() {
        return getParseUser(KEY_FOLLOWER);
    }

    public void setFollower(ParseUser follower) {
        put(KEY_FOLLOWER, follower);
    }
}

