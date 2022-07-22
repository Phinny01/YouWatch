package com.example.youwatch.fragments;

import static com.example.youwatch.fragments.PostAdapter.PROFILE_IMAGE;
import static com.example.youwatch.fragments.PostAdapter.image;
import static com.example.youwatch.fragments.PostAdapter.posts;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
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

import com.example.youwatch.Post;
import com.example.youwatch.R;
import com.example.youwatch.RelevanceAlgorithm;
import com.example.youwatch.SearchHistory;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFeedAdapter extends RecyclerView.Adapter<SearchFeedAdapter.SearchViewHolder> implements View.OnTouchListener {
    private Context context;
    private static List<Post> postList;
    public SearchFeedAdapter(Context context, List<Post> postList) {
        this.context = context;
        SearchFeedAdapter.postList = postList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        ImageView profilePicture;
        VideoView video;
        TextView userName;
        TextView caption;
        ParseFile Video;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.ivProfile);
            video = itemView.findViewById(R.id.vvPost);
            userName = itemView.findViewById(R.id.tvUser);
            caption = itemView.findViewById(R.id.tvDescription);
            itemView.setOnTouchListener(this);
            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    int position = getAdapterPosition();
                    Post post = posts.get(position);
                    SearchHistory searchHistory = new SearchHistory();
                    searchHistory.setPosts(post);
                    searchHistory.saveInBackground();
                }
            });
        }

        private void onPrepared() {
            View placeholder = itemView.findViewById(R.id.placeholder);
            placeholder.setVisibility(View.GONE);
        }

        public void bind(Post post) {
            caption.setText(post.getDescription());
            userName.setText(post.getUser().getUsername());
            Video = post.getVideo();
            image = post.getUser().getParseFile(PROFILE_IMAGE);
            Uri VideoUri = Uri.parse(Video.getUrl());
            if (image != null) {
                Picasso.with(context).load(image.getUrl()).into(profilePicture);
            }
            if (video != null) {
                video.setVideoURI(VideoUri);
                MediaController mediaController = new MediaController(itemView.getContext());
                mediaController.setAnchorView(video);
                mediaController.setMediaPlayer(video);
                video.setMediaController(mediaController);
            } else {
                video.setVideoURI(Uri.parse(""));
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            onPrepared();
            return true;
        }
    }
}
