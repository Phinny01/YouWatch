package com.example.youwatch;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("SearchHistory")
public class SearchHistory extends ParseObject {
    public static final String KEY_POSTS = "Posts";

    public Post getPosts() {
        return (Post) getParseObject(KEY_POSTS);
    }

    public void setPosts(Post posts) {
        put(KEY_POSTS, posts);
    }
}
