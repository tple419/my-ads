package com.module.adsdk;

import static androidx.lifecycle.Lifecycle.Event.ON_START;
import static com.module.adsdk.AdsSplashActivity.PrintLog;
import static com.module.adsdk.AppOpenManagerSplash.isShowingAd;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAppOpenAd;
import com.applovin.sdk.AppLovinSdk;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

public class AppOpenAdsManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String TAG = "AppOpenAdsManager";
    public static boolean isAD = false;
    private final Application myApplication;
    private AppOpenAd appOpenAd = null;
    private MaxAppOpenAd appOpenAdApplovin = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private MaxAdListener maxAdListener;
    private Activity currentActivity;

    public AppOpenAdsManager(Application myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


    public void fetchAd() {
        if (AdsHelperClass.getAdShowStatus() != 1) {
            return;
        }
        if (AdsHelperClass.getappOpenAdStatus() != 1) {
            return;
        }

        if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADMOB) || AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADX)) {
            if(appOpenAd != null){
                return;
            }
        }
        if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.APPLOVIN)) {
            if(appOpenAdApplovin != null){
                return;
            }
        }

        if (AdsHelperClass.isShowingFullScreenAd) {
            return;
        }
        if (isShowingAd) {
            return;
        }
        int showCount = AdsHelperClass.getOpenAdsShowedCount();
        int totalLimit = AdsHelperClass.getappOpenCount();
        PrintLog(TAG, "Open App Limit Exist  ShowCount: " + showCount + "  totalLimit: " + totalLimit);

        if (showCount >= totalLimit) {
            PrintLog(TAG, "Open App Limit Exist  ShowCount: " + showCount + "  totalLimit: " + totalLimit);
            return;
        }
        if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADMOB) || AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADX)) {
            loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd mAppOpenAd) {
                    super.onAdLoaded(mAppOpenAd);
                    appOpenAd = mAppOpenAd;
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    appOpenAd = null;
                    PrintLog(TAG, "error in loading");
                }
            };

            AdRequest request = getAdRequest();
            if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADMOB)) {
                if (AdsHelperClass.getAdmobAppOpenId().isEmpty()) {
                    return;
                }
                AppOpenAd.load(myApplication, AdsHelperClass.getAdmobAppOpenId(), request,/* AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,*/ loadCallback);

            } else {
                if (AdsHelperClass.getAdxAppOpenId().isEmpty()) {
                    return;
                }
                AppOpenAd.load(myApplication, AdsHelperClass.getAdxAppOpenId(), request, /*AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,*/ loadCallback);

            }
        } else if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.APPLOVIN)) {
            appOpenAdApplovin = new MaxAppOpenAd(AdsHelperClass.getapplovininter(), myApplication);
            maxAdListener = new MaxAdListener() {
                @Override
                public void onAdLoaded(MaxAd ad) {
                    PrintLog(TAG, "onAppOpenAdToLoad: ");
                }

                @Override
                public void onAdDisplayed(MaxAd ad) {
                    AdsHelperClass.isShowingFullScreenAd = true;
                    isShowingAd = true;
                }

                @Override
                public void onAdHidden(MaxAd ad) {
                    AdsHelperClass.isShowingFullScreenAd = false;
                    appOpenAdApplovin = null;
                    isShowingAd = false;
                    fetchAd();
                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    AdsHelperClass.isShowingFullScreenAd = false;
                    appOpenAdApplovin = null;
                    isShowingAd = false;
                    fetchAd();
                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    AdsHelperClass.isShowingFullScreenAd = false;
                    appOpenAdApplovin = null;
                    isShowingAd = false;
                    fetchAd();
                }
            };
            appOpenAdApplovin.setListener(maxAdListener);
            appOpenAdApplovin.loadAd();
        }
    }

    public void showAdIfAvailable() {

        if (AdsHelperClass.getAdShowStatus() != 1) {
            return;
        }
        if (AdsHelperClass.getappOpenAdStatus() != 1) {
            return;
        }

        if (AdsHelperClass.isShowingFullScreenAd) {
            return;
        }
        if (isShowingAd) {
            return;
        }


        if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADMOB) || AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADX)) {
            if(appOpenAd != null ) {
                PrintLog(TAG, "Will show ad.");
                FullScreenContentCallback fullScreenContentCallback =
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                PrintLog(TAG, "onAdDismissedFullScreenContent");

                                // Set the reference to null so isAdAvailable() returns false.
                                AdsHelperClass.isShowingFullScreenAd = false;
                                appOpenAd = null;
                                isShowingAd = false;
                                fetchAd();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                PrintLog(TAG, "onAdFailedToShowFullScreenContent: Error:  "+adError.getMessage() + "    Code: "+adError.getCode());
                                AdsHelperClass.isShowingFullScreenAd = false;
                                appOpenAd = null;
                                isShowingAd = false;
                                fetchAd();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                AdsHelperClass.isShowingFullScreenAd = true;
                                isShowingAd = true;
                            }
                        };

                appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                appOpenAd.show(currentActivity);
                int count = AdsHelperClass.getOpenAdsShowedCount();
                AdsHelperClass.setOpenAdsShowedCount(count + 1);
            }else {
                fetchAd();
            }

        } else if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.APPLOVIN)) {
            if(appOpenAdApplovin != null) {
                PrintLog(TAG, "Will show ad.");
                if (!AppLovinSdk.getInstance(myApplication).isInitialized()) {
                    return;
                }
                if (appOpenAdApplovin.isReady()) {
                    appOpenAdApplovin.showAd();
                    int count = AdsHelperClass.getOpenAdsShowedCount();
                    AdsHelperClass.setOpenAdsShowedCount(count + 1);
                }else {
                    PrintLog(TAG, "FullScreenContent: is Not ready to Show App Lovin app open");
                }
            }else {
                fetchAd();
            }
        }
    }


    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;


    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
    }


    @OnLifecycleEvent(ON_START)
    public void onStart() {
        PrintLog(TAG, "onStart");
        if (isAD) {
            //load ads
            fetchAd();
        } else {
            // if available than show
            showAdIfAvailable();
        }
    }
}
