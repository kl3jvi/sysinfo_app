package com.example.sysinfo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.sysinfo.R;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView imageView = findViewById(R.id.iccon);
        imageView.setImageResource(R.drawable.ic_splash);

        new Handler().postDelayed(() -> {
            Intent i= new Intent(SplashScreen.this,MainActivity.class);
            startActivity(i); //start new activity
            finish();
        }, SPLASH_DISPLAY_LENGTH); //time in milliseconds

    }
}