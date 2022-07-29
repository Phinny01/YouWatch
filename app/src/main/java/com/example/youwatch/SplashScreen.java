package com.example.youwatch;

import static com.example.youwatch.R.anim.fade_in;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    Handler handler;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(fade_in, R.anim.fade_out);
                finish();
            }
        }, 3000);
    }
}