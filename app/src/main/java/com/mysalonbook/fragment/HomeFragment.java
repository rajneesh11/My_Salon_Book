package com.mysalonbook.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mysalonbook.R;
import com.mysalonbook.activity.MainActivity;
import com.mysalonbook.sharedprefs.SessionManager;
import com.mysalonbook.utils.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {
    private final MainActivity mainActivity;
    private SessionManager sessionManager;
    private RequestQueue requestQueue;
    private DatePickerDialog datePickerDialog;

    public HomeFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(mainActivity);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fabNewBooking = view.findViewById(R.id.fab_new_booking);
        if (sessionManager.getUserId().equalsIgnoreCase("admin"))
            fabNewBooking.setVisibility(View.GONE);

        fabNewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                View mView = mainActivity.getLayoutInflater().inflate(R.layout.dialog_new_booking, null);
                builder.setTitle("Create a new Booking");
                Spinner jobSpinner = mView.findViewById(R.id.spinner_job);
                EditText bookDateEditText = mView.findViewById(R.id.et_book_date);
                Spinner slotSpinner = mView.findViewById(R.id.spinner_slot);

                ArrayAdapter<String> jobArrayAdapter = new ArrayAdapter<>(mainActivity,
                        android.R.layout.simple_spinner_item,
                        mainActivity.getResources().getStringArray(R.array.salon_jobs));
                jobArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                jobSpinner.setAdapter(jobArrayAdapter);

                ArrayAdapter<String> slotArrayAdapter = new ArrayAdapter<>(mainActivity,
                        android.R.layout.simple_spinner_item,
                        mainActivity.getResources().getStringArray(R.array.salon_slots));
                slotArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                slotSpinner.setAdapter(slotArrayAdapter);

                bookDateEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                        // date picker dialog
                        datePickerDialog = new DatePickerDialog(mainActivity,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // set day of month , month and year value in the edit text
                                        String date = String.valueOf(year);
                                        if (monthOfYear + 1 < 10) date += "-0" + (monthOfYear + 1);
                                        else date += "-" + (monthOfYear + 1);

                                        if (dayOfMonth < 10) date += "-0" + dayOfMonth;
                                        else date += "-" + dayOfMonth;

                                        bookDateEditText.setText(date);

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });

                builder.setPositiveButton("Book", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!jobSpinner.getSelectedItem().toString().equalsIgnoreCase("--Select Job--") &&
                                !slotSpinner.getSelectedItem().toString().equalsIgnoreCase("--Select Slot--") &&
                                !bookDateEditText.getText().toString().isEmpty()) {
//                            mainActivity.showToast(jobSpinner.getSelectedItem().toString() + ", " +
//                                    bookDateEditText.getText().toString() + ", " +
//                                    slotSpinner.getSelectedItem().toString());
                            createBookingApi(jobSpinner.getSelectedItem().toString(),
                                    bookDateEditText.getText().toString(),
                                    String.valueOf(slotSpinner.getSelectedItemPosition()));
                        }
                        dialogInterface.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setView(mView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        ((TextView) view.findViewById(R.id.user_name)).setText(new SessionManager(mainActivity).getUserName());
        requestQueue = Volley.newRequestQueue(mainActivity);

        getTodayBookingsApi(view);
    }

    private void getTodayBookingsApi(@NonNull View view) {
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
                mainActivity.stopProgressBar();
                error.printStackTrace();
                mainActivity.showToast("Some error occurred, please try again!");
            }
        });
        requestQueue.add(request);
    }

    private void createBookingApi(String job, String bookedDate, String slot) {
        mainActivity.showProgressBar();
        Map<String, String> params = new HashMap<>();
        params.put("job", job);
        params.put("booked_date", bookedDate);
        params.put("slot", slot);
        params.put("user", sessionManager.getUserId());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_BOOKING_CREATE, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mainActivity.stopProgressBar();
                            mainActivity.showToast(response.getString("message"));
                        } catch (JSONException e) {
                            mainActivity.stopProgressBar();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mainActivity.stopProgressBar();
                error.printStackTrace();
                mainActivity.showToast("Some error occurred, please try again!");
            }
        });

        requestQueue.add(request);
    }
}
