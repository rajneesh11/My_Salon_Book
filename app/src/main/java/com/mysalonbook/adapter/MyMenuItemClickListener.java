package com.mysalonbook.adapter;

import android.view.MenuItem;
import android.widget.PopupMenu;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mysalonbook.R;
import com.mysalonbook.activity.MainActivity;
import com.mysalonbook.utils.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
    private final int id;
    private final MainActivity mainActivity;
    private final ShowBookingAdapter showBookingAdapter;
    public MyMenuItemClickListener(int id, MainActivity mainActivity, ShowBookingAdapter showBookingAdapter) {
        this.id = id;
        this.mainActivity = mainActivity;
        this.showBookingAdapter = showBookingAdapter;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_cancel:
                hitApi("Cancelled");
                return true;
            case R.id.menu_done:
                hitApi("Completed");
                return true;
        }
        return false;
    }

    public void hitApi(String from) {
        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        Map<String, String> params = new HashMap<>();
        if(from.equalsIgnoreCase("Cancelled")) {
            params.put("id", ""+this.id);
            params.put("cancelled", ""+1);
        } else {
            params.put("id", ""+this.id);
            params.put("job_served", ""+1);
        }
        mainActivity.showProgressBar();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_BOOKING_UPDATE,
                new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
//                    if(response.getBoolean("status")) {
                        mainActivity.stopProgressBar();
                        mainActivity.showToast(response.getString("message"));
//                    } else {
//                        mainActivity.stopProgressBar();
//                    }
                    showBookingAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    mainActivity.stopProgressBar();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mainActivity.stopProgressBar();
            }
        }) {
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
