package com.kinfo.pixelart;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.kinfo.pixelart.activity.HomeNavigationActivity;

/**
 * Created by kinfo on 4/13/2018.
 */

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    String u_id="";
    SharedPreferences preferences;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                    Intent intent = new Intent(SplashActivity.this, HomeNavigationActivity.class);
                    startActivity(intent);
                    finish();
            }
        }, SPLASH_TIME_OUT);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }





}

