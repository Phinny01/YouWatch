package com.example.youwatch.fragments;

import static com.example.youwatch.Location.REQUEST_LOCATION;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.youwatch.Location;
import com.example.youwatch.LoginActivity;
import com.example.youwatch.Post;
import com.example.youwatch.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class timelineFragment extends Fragment {
    protected PostAdapter adapter;
    protected List<Post> allPosts;
    RecyclerView rvTimeline;

    public timelineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTimeline = view.findViewById(R.id.rvTimeline);
        allPosts = new ArrayList<>();
        adapter = new PostAdapter(getContext(), allPosts);
        rvTimeline.setAdapter(adapter);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                Location.saveCurrentUserLocation(getActivity(), getContext());
                break;
        }
    }
}
