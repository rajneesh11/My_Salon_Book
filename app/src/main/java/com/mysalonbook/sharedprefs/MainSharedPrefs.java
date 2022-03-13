package com.mysalonbook.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

public class MainSharedPrefs {
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static MainSharedPrefs sMainSharedPrefs;

    public MainSharedPrefs(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences("GSS_PREFS", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static MainSharedPrefs getInstance(Context mContext) {
        if (sMainSharedPrefs == null) sMainSharedPrefs = new MainSharedPrefs(mContext);
        return sMainSharedPrefs;
    }

    public void removePrefs() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setReceiptNo(String receiptNo) {
        editor.putString("receipt_no", receiptNo).apply();
    }

    public String getReceiptNo() {
        return sharedPreferences.getString("receipt_no", "");
    }
}
