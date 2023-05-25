package com.module.adsdk;


import android.content.Context;
import android.content.SharedPreferences;



public class SharedPreferencesClass {

    private static SharedPreferencesClass instance;
    private final SharedPreferences settings;
    private final SharedPreferences.Editor editor;

    private SharedPreferencesClass() {
        settings = MyApplicationAds.getContext().getSharedPreferences("MY_PREFERANCE_ADS", 0);
        editor = settings.edit();
    }

    public static SharedPreferencesClass getInstance() {
        if (instance == null)
            instance = new SharedPreferencesClass();
        return instance;
    }

    public static SharedPreferencesClass getInstance(Context context) {
        if (instance == null)
            instance = new SharedPreferencesClass();
        return instance;
    }

    public String getString(String key, String defValue) {
        return settings.getString(key, defValue);
    }

    public SharedPreferencesClass setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
        return this;
    }

    public SharedPreferencesClass setStatus(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
        return this;
    }

    public int getInt(String key, int defValue) {
        return settings.getInt(key, defValue);
    }

    public SharedPreferencesClass setInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
        return this;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return settings.getBoolean(key, defValue);
    }

    public SharedPreferencesClass setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
        return this;
    }

    public SharedPreferencesClass setLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
        return this;
    }

    public long getLong(String key, long defValue) {
        return settings.getLong(key, defValue);
    }

    public void clearData() {

        editor.clear();
        editor.commit();
    }

}
