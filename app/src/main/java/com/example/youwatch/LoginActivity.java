package com.example.youwatch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button loginButton;
    private Button btCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        loginButton = findViewById(R.id.button);
        btCreate = findViewById(R.id.btCreate);

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSignUp();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password, v.getContext());
            }
        });
    }

    private void loginUser(String username, String password, Context context) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Toast.makeText(LoginActivity.this, R.string.loginIssue, Toast.LENGTH_SHORT).show();
                } else {
                    locationManager.saveCurrentUserLocation(context);
                    Toast.makeText(LoginActivity.this, R.string.loginSuccess, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void goMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ParsePush.subscribeInBackground("" + ParseUser.getCurrentUser().getUsername());
    }

    private void goSignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}
