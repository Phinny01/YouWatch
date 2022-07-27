package com.example.youwatch;

import android.content.Context;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SendCallback;

public class NotificationManager {
    public static void FollowPushNotification(ParseUser User, ParseUser user, Context context, Post post) {
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
}
