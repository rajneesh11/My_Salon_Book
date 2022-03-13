package com.mysalonbook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mysalonbook.R;
import com.mysalonbook.activity.MainActivity;
import com.mysalonbook.adapter.ViewPager2Adapter;

import java.util.ArrayList;

public class BookingFragment extends Fragment {
    private ViewPager2 viewPager2;
    private final MainActivity mainActivity;
    public BookingFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        viewPager2 = view.findViewById(R.id.viewpager2);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        String[] titles = {"Today's Bookings", "Old Bookings"};
        setViewPagerAdapter();
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        }).attach();
    }

    public void setViewPagerAdapter() {
        ViewPager2Adapter adapter = new ViewPager2Adapter(mainActivity);
        ArrayList<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(new TodayBookingsFragment(mainActivity));
        fragmentList.add(new OldBookingsFragment(mainActivity));

        adapter.setData(fragmentList);
        viewPager2.setAdapter(adapter);
    }
}
