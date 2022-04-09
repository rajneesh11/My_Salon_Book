package com.mysalonbook.fragment;

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
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mysalonbook.R;
import com.mysalonbook.activity.LoginSignupActivity;
import com.mysalonbook.utils.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpFragment extends Fragment {
    @NotEmpty
    private EditText etName;

    @NotEmpty
    private EditText etEmail;

    @NotEmpty
    private EditText etAddress;

    @NotEmpty
    @Password
    private EditText etPassword;

    @NotEmpty
    @ConfirmPassword
    private EditText etConfirmPassword;

    private LoginSignupActivity activity;
    private Validator validator;

    public SignUpFragment(LoginSignupActivity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        etName = view.findViewById(R.id.sign_up_name);
        etEmail = view.findViewById(R.id.sign_up_email);
        etAddress = view.findViewById(R.id.sign_up_address);
        etPassword = view.findViewById(R.id.sign_up_password);
        etConfirmPassword = view.findViewById(R.id.sign_up_password_confirm);

        validator = new Validator(this);
        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        view.findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                if (activity.isInternetConnected())
                    registerAPI(requestQueue);
                else
                    Toast.makeText(activity, "No Internet Connection!", Toast.LENGTH_SHORT).show();
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

    private void registerAPI(RequestQueue requestQueue) {
        activity.showProgressBar();

        Map<String, String> params = new HashMap<>();
        params.put("name", etName.getText().toString());
        params.put("email", etEmail.getText().toString());
        params.put("address", etAddress.getText().toString());
        params.put("password", etPassword.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_SIGN_UP, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                activity.stopProgressBar();
                try {
                    if (response.getBoolean("status")) {
                        Toast.makeText(activity, "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
//                        activity.getSupportFragmentManager().popBackStack();
                        activity.loadLoginFrags(new LoginFragment(activity), 0.5f, 1f);
                    } else {
                        String errorMsg = response.getString("status");
                        Toast.makeText(activity, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    activity.stopProgressBar();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                activity.stopProgressBar();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
