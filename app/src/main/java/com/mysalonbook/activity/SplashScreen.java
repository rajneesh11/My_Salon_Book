package com.mysalonbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.mysalonbook.sharedprefs.SessionManager;


public class SplashScreen extends MyBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_splash);
//        setHindiFont(R.id.main_app_head_s);
//        setAppHeadGradient(R.id.main_app_head_s);

//        startIVAnimation(SplashScreen.this, findViewById(R.id.iv_splash));
        if (new SessionManager(getApplicationContext()).isLoggedIn())
            startAct(new Intent(SplashScreen.this, MainActivity.class));
        else startAct(new Intent(SplashScreen.this, LoginSignupActivity.class));

    }

    private void startAct(final Intent intent) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Pair<View, String>[] pairs = new Pair[2];
//                pairs[0] = new Pair(findViewById(R.id.main_app_head_s), "app_name");
//                pairs[1] = new Pair(findViewById(R.id.god_rem), "gss_ns");
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pairs);
                startActivity(intent);

                finish();
            }
        }, 2000);
    }

}