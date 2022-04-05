package com.mysalonbook.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.mysalonbook.R;
import com.mysalonbook.fragment.LoginFragment;
import com.mysalonbook.fragment.SignUpFragment;


public class LoginSignupActivity extends MyBaseActivity implements View.OnClickListener {
    private Button btnLoginFragment, btnSignUpFragment;
    public LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLoginFragment = findViewById(R.id.fbtn_login_head);
        btnSignUpFragment = findViewById(R.id.fbtn_signup_head);
        btnLoginFragment.setOnClickListener(this);
        btnSignUpFragment.setOnClickListener(this);
        progressBar = findViewById(R.id.load_bar_l);
        setProgressBar(progressBar);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment(LoginSignupActivity.this)).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fbtn_login_head:
                loadLoginFrags(new LoginFragment(LoginSignupActivity.this), 0.5f, 1f);
                break;

            case R.id.fbtn_signup_head:
                loadLoginFrags(new SignUpFragment(LoginSignupActivity.this), 1f, 0.5f);
        }
    }

    public void loadLoginFrags(Fragment f, float btnSAl, float btnLAl) {
        btnLoginFragment.setAlpha(btnLAl);
        btnSignUpFragment.setAlpha(btnSAl);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, f)
//                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean isInternetConnected() {
        return super.isInternetConnected();
    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();
    }

    @Override
    public void stopProgressBar() {
        super.stopProgressBar();
    }
}