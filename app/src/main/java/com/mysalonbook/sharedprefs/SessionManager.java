package com.mysalonbook.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "my_salon_Login";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        // commit changes
        editor.commit();
//        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setUserName(String username) {
        editor.putString("username", username);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString("username", "");
    }

    public void setUserId(String uid) {
        editor.putString("uid", uid);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString("uid", "");
    }

    public void setUserPhone(String phone){
        editor.putString("phone", phone);
        editor.commit();
    }

    public String getUserPhone(){
        return pref.getString("phone", "");
    }

    public void setUserAddress(String address){
        editor.putString("address", address);
        editor.commit();
    }

    public String getUserAddress(){
        return pref.getString("address", "");
    }
}
