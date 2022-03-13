package com.mysalonbook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mysalonbook.R;
import com.mysalonbook.activity.MainActivity;
import com.mysalonbook.adapter.ShowBookingAdapter;
import com.mysalonbook.model.Booking;
import com.mysalonbook.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TodayBookingsFragment extends Fragment {
    private ListView lvTodayBookingsList;
    private ArrayList<Booking> bookingList;
    private TextView tvNoBookings;
    private RequestQueue requestQueue;
    private final MainActivity mainActivity;

    public TodayBookingsFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        requestQueue = Volley.newRequestQueue(mainActivity);
        bookingList = new ArrayList<>();
        lvTodayBookingsList = view.findViewById(R.id.lv_today_bookings);
        tvNoBookings = view.findViewById(R.id.tv_no_booking_today);
        fetchBookingData();
    }

    private void fetchBookingData() {
        Map<String, String> params = new HashMap<>();
        params.put("bookings_data", "today");
        params.put("user", "my_salon_owner");
        mainActivity.showProgressBar();
        bookingList.clear();
        ShowBookingAdapter bookingAdapter = new ShowBookingAdapter(bookingList, mainActivity, "today");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_BOOKING_READ, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                mainActivity.stopProgressBar();
                                lvTodayBookingsList.setVisibility(View.VISIBLE);
                                tvNoBookings.setVisibility(View.GONE);

                                JSONArray jsonArray = response.getJSONArray("records");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject o = jsonArray.optJSONObject(i);
                                    Booking booking = new Booking(Integer.parseInt(o.getString("id")), o.getString("booked_date"),
                                            o.getString("job"), Integer.parseInt(o.getString("slot")), o.getString("booked_on"),
                                            o.getString("user"), Integer.parseInt(o.getString("cancelled")), Integer.parseInt(o.getString("job_served")));

                                    bookingList.add(booking);
                                }
                                lvTodayBookingsList.setAdapter(bookingAdapter);
                            } else {
                                mainActivity.stopProgressBar();
                                lvTodayBookingsList.setVisibility(View.GONE);
                                tvNoBookings.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            mainActivity.stopProgressBar();
                            e.printStackTrace();
                        }
                        bookingAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mainActivity.stopProgressBar();
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(request);
    }
}
