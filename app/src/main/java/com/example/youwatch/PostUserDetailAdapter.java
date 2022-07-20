package com.example.youwatch;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseFile;

import java.util.List;

public class PostUserDetailAdapter extends RecyclerView.Adapter<PostUserDetailAdapter.ViewHolder> {
    static ParseFile video;
    private static Context context;
    private List<Post> posts;

    public PostUserDetailAdapter(Context context, List<Post> posts) {
        PostUserDetailAdapter.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_detail, parent, false);
        return new PostUserDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        private VideoView vvPost;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vvPost = itemView.findViewById(R.id.vvPost);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            itemView.setOnTouchListener(this);
        }

        private void onPrepared() {
            View placeholder = itemView.findViewById(R.id.placeholder);
            placeholder.setVisibility(View.GONE);
        }

        public void bind(Post post) {
            tvDescription.setText(post.getDescription());
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
            return false;
        }
    }
}