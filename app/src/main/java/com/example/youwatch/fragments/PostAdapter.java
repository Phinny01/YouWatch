package com.example.youwatch.fragments;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.youwatch.Post;
import com.example.youwatch.R;
import com.parse.ParseFile;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public PostAdapter(Context context, List<Post> posts) {

        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUser;
        private VideoView vvPost;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            vvPost = itemView.findViewById(R.id.vvPost);
            tvUser = itemView.findViewById(R.id.tvUser);
        }
        public void bind(Post post) {

            tvDescription.setText(post.getDescription());
            tvUser.setText(post.getUser().getUsername());
            ParseFile video = post.getVideo();
            Uri VideoUri = Uri.parse(video.getUrl());

            if (video != null) {
                vvPost.setVideoURI(VideoUri);
                vvPost.start();
            } else {
                vvPost.setVideoURI(Uri.parse(""));
            }
        }
    }
}
