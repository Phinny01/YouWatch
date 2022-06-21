package com.example.youwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity {

    EditText username;
    EditText password;
    Button btnSubmit;
    public static final String message = "Something went wrong";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.user);
        password = findViewById(R.id.Password);
        btnSubmit = findViewById(R.id.signup);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ParseUser user = new ParseUser();
                String user_name = username.getText().toString();
                String Password = password.getText().toString();
                user.setUsername(user_name);
                user.setPassword(Password);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            goMainActivity();

                        } else {
                            Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    public void goMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}