package com.example.youwatch;

import android.content.Context;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import java.util.List;

public class NotificationManager {
    public static void FollowPushNotification(ParseUser User, ParseUser user, Context context) {
        ParsePush push = new ParsePush();
        push.setChannel("" + User.getUsername());
        push.setMessage(user.getUsername() + " " + context.getString(R.string.Followed) + " " + context.getString(R.string.You));
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void PostPushNotification(ParseUser User, ParseUser user, Context context) {
        ParsePush push = new ParsePush();
        push.setChannel("" + User.getUsername());
        push.setMessage(user.getUsername() + " " + context.getString(R.string.Posted));
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void user_query(Context context) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                for (ParseUser object : objects) {
                    try {
                        if (getAllFollowers(ParseUser.getCurrentUser()).contains(object.getObjectId())) {
                            PostPushNotification(object, ParseUser.getCurrentUser(), context);
                        }
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
    }

    private static String getAllFollowers(ParseUser currentUser) throws ParseException {
        Followers follow_obj = (Followers) currentUser.get(Followers.KEY_FOLLOWER);
        String allFollowers = follow_obj.fetchIfNeeded().getString(Followers.KEY_FOLLOWER);
        return allFollowers;
    }
}
