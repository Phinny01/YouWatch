package com.example.youwatch.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.example.youwatch.Post;
import com.example.youwatch.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFeedAdapter extends BaseAdapter implements ListAdapter {
    LayoutInflater inflater;
    PostAdapter.ViewHolder holder;
    private ArrayList<Post> arraylist;
    private List<Post> postsList;

    public SearchFeedAdapter(Context context, List<Post> postsList) {
        this.postsList = SearchFragment.postsList;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(postsList);
    }

    @Override
    public int getCount() {
        return postsList.size();
    }

    @Override
    public Post getItem(int position) {

        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(arraylist.get(position).getObjectId(), 36);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
            holder = new PostAdapter.ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (PostAdapter.ViewHolder) view.getTag();
        }
        return view;
    }

    public void filter(String text) {
        text.toLowerCase(Locale.ROOT);
        postsList.clear();
        if (text.length() == 0) {
            for (Post post : arraylist) {
                postsList.add(post);
                holder.bind(post);
            }
        } else {
            for (Post post : arraylist) {
                if (post.getDescription().toLowerCase(Locale.ROOT).contains(text)) {
                    postsList.add(post);
                    holder.bind(post);
                }
            }
        }
        notifyDataSetChanged();
    }
}
