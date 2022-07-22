package com.example.youwatch.fragments;


import static com.example.youwatch.SearchRelevance.getCaptionArray;
import static com.example.youwatch.locationManager.REQUEST_LOCATION;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.youwatch.Post;
import com.example.youwatch.R;
import com.example.youwatch.locationManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class timelineFragment extends Fragment {
    protected PostAdapter adapter;
    protected List<Post> allPosts;
    RecyclerView rvTimeline;
    private static final int LIMIT = 20;
    public static List<String> captions;

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
        query.setLimit(LIMIT);
        query.addDescendingOrder(Post.KEY_RELEVANCE);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                allPosts.addAll(posts);
                captions =  getCaptionArray(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SwipeRefreshLayout swipeContainer;
        super.onViewCreated(view, savedInstanceState);
        rvTimeline = view.findViewById(R.id.rvTimeline);
        allPosts = new ArrayList<>();
        adapter = new PostAdapter(getContext(), allPosts);
        rvTimeline.setAdapter(adapter);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_light);
        queryPosts();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }

            private void fetchTimelineAsync(int i) {
                adapter.clear();
                queryPosts();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                locationManager.saveCurrentUserLocation(getContext());
                break;
        }
    }
}
