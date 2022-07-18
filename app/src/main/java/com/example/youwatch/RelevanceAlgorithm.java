package com.example.youwatch;

import static com.example.youwatch.Post.KEY_LOCATION;

import android.location.Location;
import android.util.Log;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.Date;
import java.util.Objects;

public class RelevanceAlgorithm {
    private static final double DEFAULT = 100;
    private static final double TIME_RELEVANCE = 0.1;
    private static final double PROXIMITY_RELEVANCE = 0.3;
    private static final double POPULARITY_RELEVANCE = 0.6;

    public static double getRelevance(Post post) {
        ParseUser user = ParseUser.getCurrentUser();
        ParseGeoPoint user_location = user.getParseGeoPoint(KEY_LOCATION);
        double user_lat = Objects.requireNonNull(user_location).getLatitude();
        double user_long = Objects.requireNonNull(user_location).getLongitude();
        ParseGeoPoint post_location = post.getLocation();
        double post_lat = post_location.getLatitude();
        double post_long = post_location.getLongitude();
        double time_percentage = (TIME_RELEVANCE * getTime(post.getCreatedAt()));
        double proximity_percentage = (PROXIMITY_RELEVANCE * getDistance(user_lat, user_long, post_lat, post_long));
        double popularity_percentage = (POPULARITY_RELEVANCE * post.getViews());
        Double relevance = DEFAULT - time_percentage - proximity_percentage + popularity_percentage;
        return relevance;
    }

    public static void Views(Post post) {
        Post post1 = post;
        int views = post1.getViews();
        post1.setViews(views + 1);
    }

    public static double getDistance(double latitude_1, double longitude_1, double latitude_2, double longitude_2) {
        double dLat = Math.toRadians(latitude_2 - latitude_1);
        double dLon = Math.toRadians(longitude_2 - longitude_1);
        double lat1 = Math.toRadians(latitude_1);
        double lat2 = Math.toRadians(latitude_2);
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        double distance = c * rad;
        return distance;
    }

    public static long getTime(Date createdAt) {
        long HOUR_MILLIS = 60 * 60 * 1000;
        long final_time = 0;
        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();
            long diff = now - time;
            final_time = diff / HOUR_MILLIS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return final_time;
    }
}
