package com.example.youwatch;

import static com.example.youwatch.NotificationManager.FollowPushNotification;
import static com.example.youwatch.fragments.PostAdapter.PROFILE_IMAGE;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PostUserDetail extends Activity {
    RecyclerView rvProfile;
    PostUserDetailAdapter adapter;
    protected List<Post> allPosts;
    Switch follow;
    TextView username;
    ImageView profileImage;
    Post post;
    private final boolean FOLLOWED = true;
    private final boolean UNFOLLOWED = false;
    String userFollowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_user_detail);
        rvProfile = findViewById(R.id.rvProfile);
        follow = findViewById(R.id.follow);
        username = findViewById(R.id.tvUsername);
        profileImage = findViewById(R.id.ivImage);
        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        ParseUser user = post.getUser();
        username.setText(user.getUsername());
        ParseFile image = user.getParseFile(PROFILE_IMAGE);
        Followers follow_obj = (Followers) user.get(Followers.KEY_FOLLOWER);
        try {
            userFollowers = follow_obj.fetchIfNeeded().getString(Followers.KEY_FOLLOWER);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        allPosts = new ArrayList<>();
        adapter = new PostUserDetailAdapter(this, allPosts);
        rvProfile.setAdapter(adapter);
        rvProfile.setLayoutManager(new GridLayoutManager(this, 2));
        if (image != null) {
            Picasso.with(this).load(image.getUrl()).into(profileImage);
        }
        if (userFollowers.contains(currentUser.getObjectId())) {
            follow.setChecked(true);
        }
        follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked && userFollowers.contains(currentUser.getObjectId())) {
                    follow_obj.setFollower(userFollowers, UNFOLLOWED, follow_obj);
                    follow_obj.saveInBackground();
                    Toast.makeText(PostUserDetail.this, R.string.Unfollowed, Toast.LENGTH_SHORT).show();
                } else if (isChecked) {
                    follow_obj.setFollower(currentUser.getObjectId(), FOLLOWED, follow_obj);
                    follow_obj.saveInBackground();
                    Toast.makeText(PostUserDetail.this, R.string.Followed, Toast.LENGTH_SHORT).show();
                    FollowPushNotification(user, currentUser, getApplicationContext(), post);

                } else {
                    follow_obj.setFollower(userFollowers, UNFOLLOWED, follow_obj);
                    follow_obj.saveInBackground();
                    Toast.makeText(PostUserDetail.this, R.string.Unfollowed, Toast.LENGTH_SHORT).show();
                }
            }
        });
        queryPosts();
    }

    private void queryPosts() {
        ParseQuery<Post> profileQuery = ParseQuery.getQuery(Post.class);
        profileQuery.include(Post.KEY_USER);
        profileQuery.addDescendingOrder(Post.CREATED_AT);
        profileQuery.whereEqualTo(Post.KEY_USER, post.getUser());
        profileQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
