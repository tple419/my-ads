package com.module.adsdk;

import android.content.Context;

public class MyApplicationAds {

    public static Context context;
    

    public MyApplicationAds(Context mActivity) {
        context = mActivity;
    }

    public static Context getContext() {
        return context;
    }
}
