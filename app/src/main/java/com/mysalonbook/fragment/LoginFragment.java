package com.mysalonbook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mysalonbook.R;
import com.mysalonbook.activity.LoginSignupActivity;
import com.mysalonbook.activity.MainActivity;
import com.mysalonbook.sharedprefs.SessionManager;
import com.mysalonbook.utils.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginFragment extends Fragment {
    private LoginSignupActivity activity;
    @NotEmpty
    private EditText etPhone;
    @NotEmpty
    @Password
    private EditText etPassword;

    private SessionManager session;
    private Validator validator;

    public LoginFragment(LoginSignupActivity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etPhone = view.findViewById(R.id.login_phone);
        etPassword = view.findViewById(R.id.login_password);
        validator = new Validator(this);

        session = new SessionManager(activity);

        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                if (activity.isInternetConnected())
                    if (etPhone.getText().toString().equalsIgnoreCase("admin") &&
                            etPassword.getText().toString().equalsIgnoreCase("admin123")) {
                        session.setLogin(true);
                        // Now store the user in SQLite
                        session.setUserId("admin");
                        session.setUserName("My Salon");
                        session.setUserPhone("--NA--");
                        session.setUserAddress("My Salon");
                        Intent intent = new Intent(activity, MainActivity.class);
                        startActivity(intent);
                        activity.finish();
                    } else
                        checkLogin(etPhone.getText().toString(), etPassword.getText().toString(), requestQueue);
                else
                    Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String msg = error.getCollatedErrorMessage(activity);

                    if (view instanceof EditText)
                        ((EditText) view).setError(msg);
                    else Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void checkLogin(final String email, final String password, RequestQueue requestQueue) {
        activity.showProgressBar();

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_LOGIN, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String errorMsg = response.getString("message");
                            Toast.makeText(activity, errorMsg, Toast.LENGTH_LONG).show();
                            activity.stopProgressBar();
                            if (response.getBoolean("status")) {
//                                activity.stopProgressBar();
                                // user successfully logged in
                                // Create login session
                                session.setLogin(true);
                                // Now store the user in SQLite
                                session.setUserId(response.getString("id"));
                                session.setUserName(response.getString("username"));
                                session.setUserPhone(response.getString("phone"));
                                session.setUserAddress(response.getString("address"));

                                // Launch main activity
                                Intent intent = new Intent(activity, MainActivity.class);
                                startActivity(intent);
                                activity.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

        /*StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("status");

                    String errorMsg = jObj.getString("message");
                    Toast.makeText(activity, errorMsg, Toast.LENGTH_LONG).show();
                    activity.stopProgressBar();
                    // Check for error node in json
                    if (error) {
                        activity.stopProgressBar();
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("id");

//                        JSONObject user = jObj.getJSONObject("user");
                        String name = jObj.getString("username");
//                        String phone = user.getString("phone");
//                        String address = user.getString("address");
//                        String area = user.getString("area");
//                        String created_at = user.getString("created_at");

                        // Launch main activity
                        Intent intent = new Intent(activity, MainActivity.class);
                        startActivity(intent);
                        activity.finish();
                    } *//*else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(activity, errorMsg, Toast.LENGTH_LONG).show();
                        activity.stopProgressBar();
                    }*//*
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    activity.stopProgressBar();
                    Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
                activity.stopProgressBar();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", phone);
                params.put("password", password);

                return params;
            }

        };*/
    }
}
