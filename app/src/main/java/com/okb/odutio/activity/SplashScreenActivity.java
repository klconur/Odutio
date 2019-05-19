package com.okb.odutio.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.okb.odutio.R;
import com.okb.odutio.UserPreference;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int AUTO_HIDE_DELAY_MILLIS = 1000;

    private Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent;
            if (UserPreference.isLoggedIn()) {
                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mHideHandler.postDelayed(mHideRunnable, AUTO_HIDE_DELAY_MILLIS);
    }

}
