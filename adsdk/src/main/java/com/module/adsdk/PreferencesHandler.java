package com.module.adsdk;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHandler {
    private static final String MyPrefs = "MyPrefsInstall";
    public static final String MyPrefsKey1 = "Key1";




    public static void putBooleanInstall(String str, boolean z, Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(MyPrefs, 0).edit();
        edit.putBoolean(str, z);
        edit.apply();
    }
    public static Boolean getBooleanInstall(String str, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPrefs, 0);
        return sharedPreferences.getBoolean(str, false);

    }

  }
