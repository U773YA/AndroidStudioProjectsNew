package com.project.android.studentshelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

/**
 * Created by Utteya on 3/29/2018.
 */

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView image_Splash;

    SingletonClass obj = SingletonClass.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        obj.state_splash_page=true;

        image_Splash =(ImageView)findViewById(R.id.splash_image);

        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(2000);
        animation1.setFillAfter(true);
        image_Splash.startAnimation(animation1);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    String myString = QueryPreferences.getusername (getApplicationContext());
                    if (myString.isEmpty()) {
                        Intent intent =LoginPage.newIntent(getApplicationContext());
                        startActivity(intent);
                    } else {
                        Intent intent = MainPage.newIntent(getApplicationContext());
                        startActivity(intent);
                    }
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, SplashScreenActivity.class);
        return i;
    }
}
