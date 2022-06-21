package com.example.youwatch;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_VIDEO = "Video";
    public static final String KEY_USER = "User";
    public static final String CREATED_AT = "createdAt";
    public static final String KEY_CATEGORY = "Category";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getVideo() {
        return getParseFile(KEY_VIDEO);
    }

    public void setVideo(ParseFile video) {
        put(KEY_VIDEO, video);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public ParseUser getCategory() {
        return getParseUser(KEY_CATEGORY);
    }

    public void setUser(String category) {
        put(KEY_CATEGORY, category);
    }
}
