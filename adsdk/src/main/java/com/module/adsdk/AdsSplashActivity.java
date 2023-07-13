package com.module.adsdk;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class AdsSplashActivity extends AppCompatActivity {
    private AppOpenManagerSplash manager;

    public static void PrintLog(String s, String s1) {
        if (BuildConfig.DEBUG) {
            Log.d(s, s1);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_splash);


    }

    public void ADInit(String response, final Activity activity, List<String> testDeviceIds, final int cversion, final getAdsDataListner myCallback1) {
        //set in preference
        AdsHelperClass.setRemoteData(response);
        AppManage.getInstance(activity).getResponseFromPref(new getAdsDataListner() {
            @Override
            public void onSuccess() {
                 manager = new AppOpenManagerSplash(activity);
                if (AdsHelperClass.getSplash_ad_type() == 2  && isNetworkAvailable() && AdsHelperClass.getIs_splash_on() == 1 && AdsHelperClass.getIsPreloadSplashAd() == 1 ) {
                    myCallback1.onSuccess();
                }else if (AdsHelperClass.getSplash_ad_type() == 2  && isNetworkAvailable() && AdsHelperClass.getIs_splash_on() == 1) {
                            myCallback1.onSuccess();
                } else if (AdsHelperClass.getSplash_ad_type() == 1 && AdsHelperClass.getIs_splash_on()==1 && AdsHelperClass.getappOpenAdStatus() == 1 && isNetworkAvailable()) {
                    //app open load
                     AdsHelperClass.isIsShowingFullScreenAdSplash=true;
                    manager.showAdIfAvailableSplash(new AppOpenManagerSplash.splshADlistner() {
                            @Override
                            public void onSuccess() {

                                manager.showAdIfAvailableSplash(new AppOpenManagerSplash.splshADlistner() {
                                    @Override
                                    public void onSuccess() {
                                        AdsHelperClass.isIsShowingFullScreenAdSplash=false;
                                        myCallback1.onSuccess();
                                    }

                                    @Override
                                    public void onError(String error) {
                                        AdsHelperClass.isIsShowingFullScreenAdSplash=false;
                                        myCallback1.onSuccess();
                                    }
                                });

                            }

                            @Override
                            public void onError(String error) {
                                AdsHelperClass.isIsShowingFullScreenAdSplash=false;
                                myCallback1.onSuccess();
                            }
                        });

                }else {
                    myCallback1.onSuccess();
                }
            }

            @Override
            public void onUpdate(String url) {
                myCallback1.onUpdate(url);
            }

            @Override
            public void onRedirect(String url) {
                myCallback1.onRedirect(url);
            }

            @Override
            public void onReload() {
            }


        },testDeviceIds, cversion);
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}