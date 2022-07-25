package com.example.youwatch.fragments;

import static com.example.youwatch.Post.KEY_DESCRIPTION;
import static com.example.youwatch.fragments.timelineFragment.captions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youwatch.Post;
import com.example.youwatch.R;
import com.example.youwatch.SearchHistory;
import com.example.youwatch.SearchRelevance;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter = null;
    Button searchButton;
    AutoCompleteTextView searchEditText;
    public String toSearch;
    public static List<Post> postsList = new ArrayList<>();

    public SearchFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rvSearch);
        adapter = new SearchFeedAdapter(getContext(), postsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchEditText = view.findViewById(R.id.etSearch);
        searchEditText.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, captions));
        searchButton = view.findViewById(R.id.btnSearch);
        searchSuggestionQuery();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSearch = searchEditText.getText().toString();
                queryPosts(toSearch);
            }
        });
    }

    private void queryPosts(String toSearch) {
        ParseQuery<Post> postParseQuery = new ParseQuery<>(Post.class);
        postParseQuery.whereContains(KEY_DESCRIPTION, toSearch);
        postParseQuery.include(Post.KEY_USER);
        postParseQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                postsList.clear();
                if (objects.size() > 1) {
                    SearchRelevance.getOccurrence(objects, toSearch, postsList);
                } else {
                    postsList.addAll(objects);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void searchSuggestionQuery() {
        ParseQuery<Post> query = new ParseQuery<Post>(SearchHistory.KEY_POSTS);
        query.include(SearchHistory.KEY_POSTS);
        query.setLimit(1);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                postsList.addAll(objects);
                adapter.notifyDataSetChanged();
            }

        });
    }
}