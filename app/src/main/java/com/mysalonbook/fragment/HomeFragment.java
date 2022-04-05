package com.mysalonbook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mysalonbook.R;
import com.mysalonbook.activity.MainActivity;
import com.mysalonbook.sharedprefs.SessionManager;
import com.mysalonbook.utils.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {
    private MainActivity mainActivity;
    public HomeFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        ((TextView)view.findViewById(R.id.user_name)).setText(new SessionManager(mainActivity).getUserName());
        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        mainActivity.showProgressBar();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, AppConfig.URL_BOOKING_COUNT, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                TextView tvTodayBookings = view.findViewById(R.id.tv_today_bookings);
                try {
//                    System.out.println(response);
                    tvTodayBookings.setText(response.getString("message"));
                    mainActivity.stopProgressBar();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }
}
