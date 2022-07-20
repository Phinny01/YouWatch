package com.example.youwatch;

import static com.example.youwatch.fragments.PostAdapter.PROFILE_IMAGE;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_user_detail);
        rvProfile = findViewById(R.id.rvProfile);
        follow = findViewById(R.id.follow);
        username = findViewById(R.id.tvUsername);
        profileImage = findViewById(R.id.ivImage);
        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        ParseUser user = post.getUser();
        username.setText(user.getUsername());
        ParseFile image = post.getUser().getParseFile(PROFILE_IMAGE);
        allPosts = new ArrayList<>();
        adapter = new PostUserDetailAdapter(this, allPosts);
        rvProfile.setAdapter(adapter);
        rvProfile.setLayoutManager(new GridLayoutManager(this, 2));
        if (image != null) {
            Picasso.with(this).load(image.getUrl()).into(profileImage);
        }
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
