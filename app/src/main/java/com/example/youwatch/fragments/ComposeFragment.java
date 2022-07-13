package com.example.youwatch.fragments;


import static android.app.Activity.RESULT_OK;
import static com.parse.Parse.getApplicationContext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.youwatch.BuildConfig;
import com.example.youwatch.Post;
import com.example.youwatch.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.Objects;


public class ComposeFragment extends Fragment {
    EditText etCaption;
    Button btVideo;
    Button btPost;
    VideoView videoView;
    File videoFile;
    private static final String TAG = "composeFragment";
    private static final String VIDEO_FILE_NAME = "video.mp4";
    private static final int VIDEO_CAPTURE_ACTIVITY_REQUEST_CODE = 101;

    public ComposeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etCaption = view.findViewById(R.id.etCaption);
        btVideo = view.findViewById(R.id.btVideo);
        btPost = view.findViewById(R.id.btPost);
        videoView = view.findViewById(R.id.videoView);
        btVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = etCaption.getText().toString();
                if (caption.isEmpty()) {
                    Toast.makeText(getContext(), R.string.emptyCaption, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (videoFile == null || videoView == null) {
                    Toast.makeText(getContext(), R.string.noVideo, Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(caption, currentUser, videoFile);
            }
        });
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoFile = getVideoFileUri(VIDEO_FILE_NAME);
        Uri fileProvider = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", videoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, VIDEO_CAPTURE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getVideoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Toast.makeText(getContext(), R.string.noDirectory, Toast.LENGTH_SHORT).show();
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void savePost(String caption, ParseUser currentUser, File videoFile) {
        Post post = new Post();
        ParseGeoPoint location = currentUser.getParseGeoPoint(Post.KEY_LOCATION);
        post.setDescription(caption);
        post.setVideo(new ParseFile(videoFile));
        post.setUser(currentUser);
        post.getCreatedAt();
        post.setLocation(location);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getContext(), R.string.emptyCaption, Toast.LENGTH_SHORT).show();
                    return;
                }
                etCaption.setText("");
                videoView.setVideoURI(Uri.parse(""));
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == VIDEO_CAPTURE_ACTIVITY_REQUEST_CODE) {
                videoView.setVideoURI(Uri.fromFile(videoFile));
                MediaController mediaController = new MediaController(getContext());
                mediaController.setAnchorView(videoView);
                mediaController.setMediaPlayer(videoView);
                videoView.setMediaController(mediaController);
            }
        }
    }
}
