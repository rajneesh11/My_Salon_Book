package com.mysalonbook.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.mysalonbook.R;
import com.mysalonbook.fragment.BookingFragment;
import com.mysalonbook.fragment.HomeFragment;
import com.mysalonbook.fragment.ProfileFragment;

public class MainActivity extends MyBaseActivity implements View.OnClickListener {

    private LinearLayout llHome, llBooking, llProfile, progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
        loadFragment(new HomeFragment(MainActivity.this));
    }

    private void initialization() {
        llHome = findViewById(R.id.app_nav_home);
        llBooking=findViewById(R.id.app_nav_booking);
        llProfile=findViewById(R.id.app_nav_profile);
        progressBar = findViewById(R.id.load_bar);
        setProgressBar(progressBar);

        llHome.setOnClickListener(this);
        llBooking.setOnClickListener(this);
        llProfile.setOnClickListener(this);
    }

    @Override
    public void loadFragment(Fragment fragment) {
        super.loadFragment(fragment);
    }

    @Override
    public boolean isInternetConnected() {
        return super.isInternetConnected();
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg);
    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();
    }

    @Override
    public void stopProgressBar() {
        super.stopProgressBar();
    }

    @Override
    public void hideKeyboardFrom(Context context, View view) {
        super.hideKeyboardFrom(context, view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.app_nav_home:
                llHome.setBackgroundResource(R.drawable.app_nav_bg);
                llBooking.setBackgroundResource(0);
                llProfile.setBackgroundResource(0);
                loadFragment(new HomeFragment(MainActivity.this));
                break;
            case R.id.app_nav_booking:
                llHome.setBackgroundResource(0);
                llBooking.setBackgroundResource(R.drawable.app_nav_bg);
                llProfile.setBackgroundResource(0);
                loadFragment(new BookingFragment(MainActivity.this));
                break;
            case R.id.app_nav_profile:
                llHome.setBackgroundResource(0);
                llBooking.setBackgroundResource(0);
                llProfile.setBackgroundResource(R.drawable.app_nav_bg);
                loadFragment(new ProfileFragment(MainActivity.this));
                break;
        }
    }
}