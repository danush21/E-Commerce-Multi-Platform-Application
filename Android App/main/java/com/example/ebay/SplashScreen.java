package com.example.ebay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000; // Splash screen duration in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start your main activity
                Intent mainIntent = new Intent(SplashScreen.this, SplashScreen.class);
                startActivity(mainIntent);
                finish(); // Close the splash activity to prevent going back to it on back press

                Intent intent = new Intent(SplashScreen.this, ProductSearch.class);
                startActivity(intent);
                finish(); // Optionally finish the SplashActivity to prevent going back to it

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
