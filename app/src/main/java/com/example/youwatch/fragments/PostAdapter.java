package com.example.youwatch.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youwatch.Post;
import com.example.youwatch.PostUserDetail;
import com.example.youwatch.R;
import com.example.youwatch.RelevanceAlgorithm;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private static Context context;
    public static List<Post> posts;
    static ParseFile video;
    static ParseFile image;
    public static final String PROFILE_IMAGE = "ProfilePicture";
    private static final long DOUBLE_TIME = 500;
    private static long lastClickTime = 0;
    private static int clicks = 0;

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

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvUser;
        private VideoView vvPost;
        private TextView tvDescription;
        private ImageView ivProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            vvPost = itemView.findViewById(R.id.vvPost);
            tvUser = itemView.findViewById(R.id.tvUser);
            itemView.setOnClickListener(this);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            vvPost.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Post post = posts.get(getAdapterPosition());
                    RelevanceAlgorithm.Views(post);
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                        }
                    });
                }
            });
        }

        private void onPrepared() {
            View placeholder = itemView.findViewById(R.id.placeholder);
            placeholder.setVisibility(View.GONE);
        }

        public void bind(Post post) {
            tvDescription.setText(post.getDescription());
            tvUser.setText(post.getUser().getUsername());
            video = post.getVideo();
            image = post.getUser().getParseFile(PROFILE_IMAGE);
            Uri VideoUri = Uri.parse(video.getUrl());
            double postRelevance = RelevanceAlgorithm.getRelevance(post);
            post.setRelevance(postRelevance);
            if (image != null) {
                Picasso.with(context).load(image.getUrl()).into(ivProfile);
            }
            if (video != null) {
                vvPost.setVideoURI(VideoUri);
                MediaController mediaController = new MediaController(itemView.getContext());
                mediaController.setAnchorView(vvPost);
                mediaController.setMediaPlayer(vvPost);
                vvPost.setMediaController(mediaController);
            } else {
                vvPost.setVideoURI(Uri.parse(""));
            }
        }

        private void onDoubleClick(View v) {
            Intent intent = new Intent(context, PostUserDetail.class);
            context.startActivity(intent);
        }

        @Override
        public void onClick(View v) {
            long currentTimeMillis = System.currentTimeMillis();
            clicks++;
            if (currentTimeMillis - lastClickTime < DOUBLE_TIME && clicks >= 2) {
                onDoubleClick(v);
                clicks = 0;
            }
            lastClickTime = currentTimeMillis;
            onPrepared();
        }
    }
}