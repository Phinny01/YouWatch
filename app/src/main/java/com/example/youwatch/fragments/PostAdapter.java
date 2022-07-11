package com.example.youwatch.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private static Context context;
    private List<Post> posts;
    static ParseFile video;

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

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        private TextView tvUser;
        private VideoView vvPost;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            vvPost = itemView.findViewById(R.id.vvPost);
            tvUser = itemView.findViewById(R.id.tvUser);
            itemView.setOnTouchListener(this);
        }

        private void onPrepared() {
            View placeholder = itemView.findViewById(R.id.placeholder);
            placeholder.setVisibility(View.GONE);
        }

        public void bind(Post post) {
            tvDescription.setText(post.getDescription());
            tvUser.setText(post.getUser().getUsername());
            video = post.getVideo();
            Uri VideoUri = Uri.parse(video.getUrl());
            if (video != null) {
                vvPost.setVideoURI(VideoUri);
                MediaController mediaController = new MediaController(context);
                mediaController.setAnchorView(vvPost);
                mediaController.setMediaPlayer(vvPost);
                vvPost.setMediaController(mediaController);
            } else {
                vvPost.setVideoURI(Uri.parse(""));
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            onPrepared();
            return true;
        }
    }
}