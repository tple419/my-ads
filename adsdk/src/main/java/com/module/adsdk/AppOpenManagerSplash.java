package com.module.adsdk;

import static com.module.adsdk.AdsSplashActivity.PrintLog;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
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


public class AppOpenManagerSplash implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String LOG_TAG = "AppOpenManager";
    public static boolean isShowingAd = false;
    private final Activity myApplication;
    private AppOpenAd appOpenAd = null;
    private MaxAppOpenAd appOpenAdApplovin = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private MaxAdListener maxAdListener;

    private Activity currentActivity;

    public AppOpenManagerSplash(Activity myApplication) {
        this.myApplication = myApplication;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.myApplication.registerActivityLifecycleCallbacks(this);
        }
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


  public void showAdIfAvailableSplash(final splshADlistner listner) {
        if (!isShowingAd && isAdAvailable()) {
            PrintLog(LOG_TAG, "Will show ad.");

            if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADMOB) || AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADX)) {
                FullScreenContentCallback fullScreenContentCallback =
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                 PrintLog(LOG_TAG,"onAdDismissedFullScreenContent: " );

                                AppOpenManagerSplash.this.appOpenAd = null;
                                isShowingAd = false;
                                AdsHelperClass.isShowingFullScreenAd = false;
                                AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                                listner.onSuccess();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                 PrintLog(LOG_TAG, "onAdFailedToShowFullScreenContent: ");

                                isShowingAd = false;
                                AdsHelperClass.isShowingFullScreenAd = false;
                                AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                                listner.onError(adError.getMessage());
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                 PrintLog(LOG_TAG,"onAdShowedFullScreenContent: " );

                                isShowingAd = true;
                                AdsHelperClass.isShowingFullScreenAd = true;
                                AdsHelperClass.isIsShowingFullScreenAdSplash = true;
                            }
                        };

                appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                appOpenAd.show(currentActivity);

            } else if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.APPLOVIN)) {
                if (!AppLovinSdk.getInstance(myApplication).isInitialized()) {
                    return;
                }
                if (appOpenAdApplovin.isReady()) {
                    maxAdListener = new MaxAdListener() {
                        @Override
                        public void onAdLoaded(MaxAd ad) {
                             PrintLog(LOG_TAG, "onAdLoaded: APPLOVIN appopen");

                            listner.onSuccess();
                        }

                        @Override
                        public void onAdDisplayed(MaxAd ad) {
                            isShowingAd = true;
                            AdsHelperClass.isShowingFullScreenAd = true;
                            AdsHelperClass.isIsShowingFullScreenAdSplash = true;
                        }

                        @Override
                        public void onAdHidden(MaxAd ad) {
                            AppOpenManagerSplash.this.appOpenAdApplovin = null;
                            isShowingAd = false;
                            AdsHelperClass.isShowingFullScreenAd = false;
                            AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                            listner.onSuccess();
                        }

                        @Override
                        public void onAdClicked(MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(String adUnitId, MaxError error) {
                            PrintLog(LOG_TAG, "onAdLoadFailed: APPLOVIN appopen");

                            listner.onError(error.getMessage());
                            isShowingAd = false;
                            AdsHelperClass.isShowingFullScreenAd = false;
                            AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                            PrintLog(LOG_TAG, "onAdDisplayFailed: APPLOVIN appopen");

                            listner.onError(error.getMessage());
                            isShowingAd = false;
                            AdsHelperClass.isShowingFullScreenAd = false;
                            AdsHelperClass.isIsShowingFullScreenAdSplash = false;

                        }
                    };
                    appOpenAdApplovin.setListener(maxAdListener);
                    appOpenAdApplovin.showAd();
                }
            }


        } else {
            PrintLog(LOG_TAG,"showAdIfAvailable: error" );
            fetchAd(listner);
        }
    }


    public void fetchAd(final splshADlistner listner) {
        if (AdsHelperClass.getAdShowStatus() != 1) {
            return;
        }
        if (AdsHelperClass.getappOpenAdStatus() != 1) {
            listner.onSuccess();
            return;
        }

        if (isAdAvailable()) {
            PrintLog(LOG_TAG, "fetchAd: ");
             listner.onSuccess();
            return;
        }


        if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADMOB) || AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADX)) {

            loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                    super.onAdLoaded(appOpenAd);
                    AppOpenManagerSplash.this.appOpenAd = appOpenAd;
                     PrintLog(LOG_TAG, "onAppOpenAdToLoad: ");

                    listner.onSuccess();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    PrintLog(LOG_TAG, "onAppOpenAdFailedToLoad: " + loadAdError.getMessage());

                     listner.onError(loadAdError.getMessage());

                }
            };

            AdRequest request = getAdRequest();
            if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADMOB)) {
                if (AdsHelperClass.getAdmobAppOpenId().isEmpty()) {
                    return;
                }
                AppOpenAd.load(myApplication, AdsHelperClass.getAdmobAppOpenId(), request, /*AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,*/ loadCallback);

            } else {
                if (AdsHelperClass.getAdxAppOpenId().isEmpty()) {
                    return;
                }
                AppOpenAd.load(myApplication, AdsHelperClass.getAdxAppOpenId(), request, /*AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,*/ loadCallback);

            }
        } else if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.APPLOVIN)) {
            appOpenAdApplovin = new MaxAppOpenAd(AdsHelperClass.getapplovinOpenAdId(), myApplication);
            maxAdListener = new MaxAdListener() {
                @Override
                public void onAdLoaded(MaxAd ad) {
                     PrintLog(LOG_TAG, "onAdLoaded: APPLOVIN appopen");

                    listner.onSuccess();
                }

                @Override
                public void onAdDisplayed(MaxAd ad) {
                    isShowingAd = true;
                    AdsHelperClass.isShowingFullScreenAd = true;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = true;
                }

                @Override
                public void onAdHidden(MaxAd ad) {
                    AppOpenManagerSplash.this.appOpenAdApplovin = null;
                    isShowingAd = false;
                    AdsHelperClass.isShowingFullScreenAd = false;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = false;

                    listner.onSuccess();
                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {

                    PrintLog(LOG_TAG,"onAdLoadFailed: APPLOVIN appopen" );

                    listner.onError(error.getMessage());
                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                     PrintLog(LOG_TAG, "onAdDisplayFailed: APPLOVIN appopen");

                    listner.onError(error.getMessage());

                }
            };
            appOpenAdApplovin.setListener(maxAdListener);
            appOpenAdApplovin.loadAd();
        }else {
            PrintLog(LOG_TAG, "ad Type: "+AdsHelperClass.getAppOpenSequence());
            listner.onSuccess();
        }

    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }


    public boolean isAdAvailable() {
        if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADMOB) || AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.ADX)) {
            return appOpenAd != null;
        } else if (AdsHelperClass.getAppOpenSequence().equals(AdsHelperClass.APPLOVIN)) {
            return appOpenAdApplovin != null;

        } else {
            return false;

        }
    }

    @Override
    public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityPostCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityPreStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPostStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPreResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {


        currentActivity = activity;


    }

    @Override
    public void onActivityPostResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPrePaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityPostStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPreSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityPostPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPreStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityPostSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityPreDestroyed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }

    @Override
    public void onActivityPostDestroyed(@NonNull Activity activity) {

    }

    public interface splshADlistner {

        void onSuccess();

        void onError(String error);
    }


}