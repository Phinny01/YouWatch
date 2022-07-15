package com.example.youwatch;

public class RelevanceAlgorithm {
    public static void Views(Post post) {
        Post post1 = post;
        int views = post1.getViews();
        post1.setViews(views + 1);
    }
}
