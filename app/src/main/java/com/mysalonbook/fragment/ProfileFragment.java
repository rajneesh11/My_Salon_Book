package com.mysalonbook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mysalonbook.R;
import com.mysalonbook.activity.MainActivity;
import com.mysalonbook.sharedprefs.SessionManager;

public class ProfileFragment extends Fragment {
    private final MainActivity mainActivity;
    public ProfileFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SessionManager sessionManager = new SessionManager(mainActivity);
        ((TextView)view.findViewById(R.id.profile_user_name)).setText(sessionManager.getUserName());
        ((TextView)view.findViewById(R.id.profile_user_phone)).setText(sessionManager.getUserEmail());
        ((TextView)view.findViewById(R.id.profile_user_address)).setText(sessionManager.getUserAddress());
    }
}
