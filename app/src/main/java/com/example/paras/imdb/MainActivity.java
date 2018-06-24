package com.example.paras.imdb;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {
    // the predefined 4 second time for the splash screen to be displayed.
    private final int MAIN_SCREEN_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this line below will remove the notification bar from the top, making the splash screen to use  full screen .
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setting the layout of the main activity, the splash screen.
        setContentView(R.layout.activity_main);

        // an object of the Handler class to handle the time of the splash screen and where to go after that.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // intent to the home activity that is visible when splash screen time finishes.
                Intent intent = new Intent(MainActivity.this, MyHomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, MAIN_SCREEN_TIME_OUT);
    }

}

