package com.example.youwatch;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.youwatch.fragments.ComposeFragment.notify_post;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;

public class NotificationManager extends ParsePushBroadcastReceiver {
    private static final String CHANNEL_ID = "0";
    private static final int NOTIFICATION_ID = 1;

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
                        return;
                    }
                }
            }
        });
    }

    private static String getAllFollowers(ParseUser currentUser) throws ParseException {
        Followers follow_obj = (Followers) currentUser.get(Followers.KEY_FOLLOWER);
        String allFollowers = Objects.requireNonNull(follow_obj).fetchIfNeeded().getString(Followers.KEY_FOLLOWER);
        return allFollowers;
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
        ParseUser user = notify_post.getUser();
        intent = new Intent(context, PostUserDetail.class);
        intent.putExtra(ParseUser.class.getSimpleName(), Parcels.wrap(user));
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(resultPendingIntent).setSmallIcon(R.drawable.logo);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        context.startActivity(intent);
        ParseAnalytics.trackAppOpenedInBackground(intent);
    }
}
