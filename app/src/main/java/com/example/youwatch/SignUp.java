package com.example.youwatch;

import static com.example.youwatch.R.string.noPicture;
import static com.example.youwatch.fragments.PostAdapter.PROFILE_IMAGE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.File;
import java.util.Objects;

public class SignUp extends AppCompatActivity {
    EditText username;
    EditText password;
    Button btnSubmit;
    ImageView ivProfile;
    File photoFile;
    String photoFileName = "photo.jpg";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final String TAG = "SignUP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = findViewById(R.id.user);
        password = findViewById(R.id.Password);
        btnSubmit = findViewById(R.id.signup);
        ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser user = new ParseUser();
                String user_name = username.getText().toString();
                String Password = password.getText().toString();
                user.setUsername(user_name);
                user.setPassword(Password);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            saveProfilePhoto(user, photoFile);
                            locationManager.saveCurrentUserLocation(view.getContext());
                            Followers followers = new Followers();
                            followers.setUser(ParseUser.getCurrentUser());
                            followers.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        user.put(Followers.KEY_FOLLOWER, followers);
                                        user.saveInBackground();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SignUp.this, R.string.signupIssue, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void saveProfilePhoto(ParseUser currentUser, File photoFile) {
        currentUser.put(PROFILE_IMAGE, new ParseFile(photoFile));
        currentUser.saveInBackground();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivProfile.setImageBitmap(takenImage);
            } else {
                Toast.makeText(this, noPicture, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
