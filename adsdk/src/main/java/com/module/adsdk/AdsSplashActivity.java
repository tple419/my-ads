package com.module.adsdk;

import static com.module.adsdk.PreferencesHandler.MyPrefsKey1;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    boolean isTimeOut = false;
    public void ADInit(boolean isGalleryApp , String response, final Activity activity, List<String> testDeviceIds, final int cversion, final getAdsDataListner myCallback1) {
        //set in preference
        AdsHelperClass.setRemoteData(response);
        AppManage.getInstance(activity).getResponseFromPref(new getAdsDataListner() {
            @Override
            public void onSuccess() {
                if(isGalleryApp){
                    adsTask(activity);
                }else {
                    if (AdsHelperClass.getAdShowStatus() == 1 && AdsHelperClass.getIsOnLoadNative() == 0) {
                        AppManage.getInstance(activity).PreLoadNative(1);
                    }
                }
                if (AdsHelperClass.getIsBannerPreloadOn() == 1) {
                    AppManage.getInstance(activity).PreLoadBanner(activity);
                }

                manager = new AppOpenManagerSplash(activity);

                new Handler(Looper.myLooper()).postDelayed(() -> {
                    Log.e("AppStartFrom", "time out");
                    isTimeOut = true;

                    myCallback1.onTimeout();
                    return;
                }, AdsHelperClass.getSplash_time() * 1000L);

                if (AdsHelperClass.getAdShowStatus() == 1 && AdsHelperClass.getSplash_ad_type() == 2  && isNetworkAvailable() && AdsHelperClass.getIs_splash_on() == 1 && AdsHelperClass.getIsPreloadSplashAd() == 1 ) {
                   if(!isTimeOut) {
                       myCallback1.onSuccess();
                   }

                }else if (AdsHelperClass.getAdShowStatus() == 1 && AdsHelperClass.getSplash_ad_type() == 2  && isNetworkAvailable() && AdsHelperClass.getIs_splash_on() == 1) {
                    if(!isTimeOut) {
                        myCallback1.onSuccess();
                    }
                } else if (AdsHelperClass.getAdShowStatus() == 1 && AdsHelperClass.getSplash_ad_type() == 1 && AdsHelperClass.getIs_splash_on()==1 && AdsHelperClass.getappOpenAdStatus() == 1 && isNetworkAvailable()) {
                    //app open load

                    manager.showAdIfAvailableSplash(new AppOpenManagerSplash.splshADlistner() {
                            @Override
                            public void onSuccess() {
                                if(!isTimeOut) {
                                    AdsHelperClass.isIsShowingFullScreenAdSplash=true;
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


                            }

                            @Override
                            public void onError(String error) {
                                if(!isTimeOut) {
                                    AdsHelperClass.isIsShowingFullScreenAdSplash=false;
                                    myCallback1.onSuccess();
                                }

                            }
                        });

                }else {
                    if(!isTimeOut) {
                        myCallback1.onSuccess();
                    }
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

            @Override
            public void onTimeout() {

            }


        },testDeviceIds, cversion);
    }

    public void adsTask( Activity activity){
        AppOpenAdsManager.isAD = false;
        if (PreferencesHandler.getBooleanInstall(MyPrefsKey1, activity)) {
            AdsHelperClass.setFirst_ad_hide(1);
        } else {
            AdsHelperClass.setFirst_ad_hide(AdsHelperClass.getFirst_ad_hide());
        }

        if (AdsHelperClass.getAdShowStatus() == 1 && AdsHelperClass.isUserFirstTime()) {
            AdsHelperClass.setIsUserFirstTime(false);
            AppManage.getInstance(activity).PreLoadNative(1);
        }else {
            if (!AdsHelperClass.getShowIntro()){
                AppManage.getInstance(activity).PreLoadNative(1);
            }else {
                if (AdsHelperClass.getAdShowStatus() == 1 && AdsHelperClass.getIsOnLoadNative() == 0) {
                    AppManage.getInstance(activity).PreLoadNative(1);
                }
            }
        }
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