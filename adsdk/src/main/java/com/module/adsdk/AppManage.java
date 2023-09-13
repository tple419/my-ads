package com.module.adsdk;

import static com.module.adsdk.AdsSplashActivity.PrintLog;
import static com.module.adsdk.BuildConfig.DEBUG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class AppManage {
    private static final String TAG = "AppManage";
    public static AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    public static int count_click = -1;
    public static int nativeSmallAdSize = 150;
    public static int nativeAdSize = 270;
    public static int showTime = 0;
    public static SharedPreferences mysharedpreferences;
    public static MaxAd nativeAd2;
    public static int oldShowcount = -1;
    public static boolean isFirstTimeShow = false;
    static Activity activity;
    static MyInterstitialCallback intermyCallback;
    private static AppManage mInstance;
    public AdManagerInterstitialAd mAdManagerInterstitialAd;
    public AdManagerInterstitialAd mAdManagerInterstitialAdPre;
    public ConsentInformation consentInformation;
    ArrayList<String> banner_sequence = new ArrayList<>();
    ArrayList<String> native_sequence = new ArrayList<>();
    ArrayList<String> interstitial_sequence = new ArrayList<>();
    IronSourceBannerLayout mIronSourceBannerLayout;
    IronSourceBannerLayout mIronSourceNativeLayout;
    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInterstitialAdPre = null;
    private com.facebook.ads.InterstitialAd mFbInterstitialAd;
    private com.facebook.ads.InterstitialAd mFbInterstitialAdPre = null;
    private MaxInterstitialAd appLovin_interstitialAd;
    private MaxInterstitialAd appLovin_interstitialAdPre = null;
    private Dialog dialog;
    private boolean isIronNativePreloaded = false;
    private com.facebook.ads.NativeAd preFbNative;
    private NativeAd preAdmobNative;

    private NativeAd preAdxNative;
    private IronSourceBannerLayout preIronSourceNativeLayout;
    private MaxNativeAdView preMaxNativeAdView;

    public AppManage(Activity activity) {
        AppManage.activity = activity;
        mysharedpreferences = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        getResponseFromPref();
    }

    public static AppManage getInstance(Activity activity) {
        AppManage.activity = activity;
        if (mInstance == null) {
            mInstance = new AppManage(activity);
        }
        return mInstance;
    }

    public static boolean hasActiveInternetConnection(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void getResponseFromPref() {
        String response1 = AdsHelperClass.getRemoteData();
        if (!response1.isEmpty()) {
            AdsModel appData = parseAppUserListModel(response1);
            AdsHelperClassAdd(appData);

        }


    }

    private static void AdsHelperClassAdd(AdsModel appData) {
        AdsHelperClass.setAdShowStatus(appData.getAdShow_adstatus());
        AdsHelperClass.setAdmobAdStatus(appData.getAdmob_adstatus());
        AdsHelperClass.setAdXAdStatus(appData.getAdX_adstatus());
        AdsHelperClass.setApplovinAdStatus(appData.getAppLovin_adstatus());
        AdsHelperClass.setIronSourceAdStatus(appData.getIronSource_adstatus());
        AdsHelperClass.setFBAdStatus(appData.getFb_adstatus());
        AdsHelperClass.setbannerAdStatus(appData.getBanner_adstatus());
        AdsHelperClass.setnativeAdStatus(appData.getNative_adstatus());
        AdsHelperClass.setinterAdStatus(appData.getInter_adstatus());
        AdsHelperClass.setappOpenAdStatus(appData.getAppOpen_adstatus());
        AdsHelperClass.setappOpenCount(appData.getAppOpen_count());
        AdsHelperClass.setFullScreenLimitCount(appData.getFullScreen_count());
        AdsHelperClass.setinterstitialCount(appData.getInterstitial_count());
        AdsHelperClass.setInterstitialCountMultiplier(appData.getInterstitialCountMultiplier());
        AdsHelperClass.setExitAdEnable(appData.getExitAdEnable());
        AdsHelperClass.setIs_splash_on(appData.getIs_splash_on());
        AdsHelperClass.setSplash_ad_type(appData.getSplash_ad_type());
        AdsHelperClass.setAdmobBannerId(appData.getAdmob_bannerid());
        AdsHelperClass.setSplash_time(appData.getSplash_time());
        AdsHelperClass.setAdmobNativeId(appData.getAdmob_nativeid());
        AdsHelperClass.setAdmobInterId(appData.getAdmob_interid());
        AdsHelperClass.setAdmobAppOpenId(appData.getAdmob_appopenid());
        AdsHelperClass.setAdxBannerId(appData.getAdx_bannerid());
        AdsHelperClass.setAdxNativeId(appData.getAdx_nativeid());
        AdsHelperClass.setAdxInterId(appData.getAdx_interid());

        AdsHelperClass.setFBBannerId(appData.getFBBanner());
        AdsHelperClass.setFBNativeId(appData.getFBNative());
        AdsHelperClass.setFBInterId(appData.getFBInter());

        AdsHelperClass.setAdxAppOpenId(appData.getAdx_appopenid());
        AdsHelperClass.setapplovinbanner(appData.getApplovin_bannerid());
        AdsHelperClass.setapplovinnative(appData.getApplovin_nativeid());
        AdsHelperClass.setapplovininter(appData.getApplovin_interid());
        AdsHelperClass.setapplovinOpenAdId(appData.getApplovin_OpenAdId());
        AdsHelperClass.setironappkey(appData.getIronappkey());
        AdsHelperClass.setbannerSequence(appData.getBanner_sequence());
        AdsHelperClass.setnativeSequence(appData.getNative_sequence());
        AdsHelperClass.setInterSequence(appData.getInter_sequence());
        AdsHelperClass.setSplashAdsSequence(appData.getSplashAdsSequence());
        AdsHelperClass.setAppOpenSequence(appData.getAppOpen_sequence());
        AdsHelperClass.setapp_newPackageName(appData.getApp_newPackageName());
        AdsHelperClass.setapp_redirectOtherAppStatus(appData.getApp_redirectOtherAppStatus());
        AdsHelperClass.setapp_updateAppDialogStatus(appData.getApp_updateAppDialogStatus());
        AdsHelperClass.setapp_versionCode(appData.getApp_versionCode());
        AdsHelperClass.setapp_email_id(appData.getApp_email_id());
        AdsHelperClass.setapp_policy_url(appData.getApp_policy_url());
        AdsHelperClass.setIn_appreview(appData.getIn_appreview());
        AdsHelperClass.setFirst_ad_hide(appData.getFirst_ad_hide());
        AdsHelperClass.setIsPreloadSplashAd(appData.getIsPreloadSplashAd());
        AdsHelperClass.setIsBannerSpaceVisible(appData.getIsBannerSpaceVisible());
        AdsHelperClass.setIsOnLoadNative(appData.getIsOnLoadNative());
        AdsHelperClass.setInterstitialFirstClick(appData.getInterstitialFirstClick());

        /*New field*/
        AdsHelperClass.setS_api(appData.getS_api());
        AdsHelperClass.setS_frame_back(appData.getS_frame_back());
        AdsHelperClass.setS_sticker(appData.getS_sticker());
        AdsHelperClass.setS_thumb(appData.getS_thumb());
        AdsHelperClass.setS_Zip(appData.getS_Zip());
        AdsHelperClass.setBase(appData.getBase());
        AdsHelperClass.setPip_sticker(appData.getPip_sticker());
        AdsHelperClass.setAlbum_click_enabled(appData.getAlbum_click_enabled());
        AdsHelperClass.setDirect_review_enable(appData.getDirect_review_enable());
        AdsHelperClass.setReview_popup_count(appData.getReview_popup_count());
        AdsHelperClass.setBanner_display_pager(appData.getBanner_display_pager());
        AdsHelperClass.setBanner_display_list(appData.getBanner_display_list());
        AdsHelperClass.setNative_display_list(appData.getNative_display_list());
        AdsHelperClass.setIsGDPROn(appData.getIsGDPROn());
        AdsHelperClass.setIsGDPROnFailed(appData.getIsGDPROnFailed());

    }

    private static boolean checkUpdate(int cversion) {
        if (AdsHelperClass.getapp_updateAppDialogStatus() == 1) {
            String versions = AdsHelperClass.getapp_versionCode();
            return !versions.equals(("" + cversion));
        } else {
            return false;
        }
    }

    public static AdsModel parseAppUserListModel(String jsonObject) {
        try {
            Gson gson = new Gson();
            TypeToken<AdsModel> token = new TypeToken<AdsModel>() {
            };
            AdsModel couponModel = gson.fromJson(jsonObject, token.getType());
            return couponModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AdSize getAdSize(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    private static MaxNativeAdView createNativeAdView(Context activity) {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.ads_native_applovin_adview)
                .setTitleTextViewId(R.id.title_text_view)
                .setBodyTextViewId(R.id.body_text_view)
                .setAdvertiserTextViewId(R.id.advertiser_text_view)
                .setIconImageViewId(R.id.icon_image_view)
                .setMediaContentViewGroupId(R.id.media_view_container)
                .setOptionsContentViewGroupId(R.id.options_view)
                .setCallToActionButtonId(R.id.cta_button)
                .build();

        return new MaxNativeAdView(binder, activity);
    }

    private void initAd(getAdsDataListner listner, List<String> testDeviceIds) {


        if (AdsHelperClass.getFBAdStatus() == 1) {

            AudienceNetworkAds.buildInitSettings(activity).withInitListener(new AudienceNetworkAds.InitListener() {
                @Override
                public void onInitialized(AudienceNetworkAds.InitResult initResult) {
                    PrintLog(TAG, initResult.getMessage() + " :fb");


                }
            }).initialize();
            if (DEBUG) {
                AdSettings.turnOnSDKDebugger(activity);
                AdSettings.addTestDevice("4bd00c31-5290-4b84-a7ce-8f7c92df2da0");
            }

        }

        if (AdsHelperClass.getApplovinAdStatus() == 1) {
            AppLovinSdk.getInstance(activity).setMediationProvider("max");

            AppLovinSdk.initializeSdk(activity, new AppLovinSdk.SdkInitializationListener() {
                @Override
                public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                    PrintLog(TAG, "onSdkInitialized: " + configuration.getConsentDialogState() + " :applovin");

                }
            });


            AppLovinSdk.getInstance(activity).getSettings().setVerboseLogging(true);
            AppLovinSdk.getInstance(activity).getSettings().setTestDeviceAdvertisingIds(
                    Arrays.asList("ff177005-6c8f-4275-adab-f9f4faf4711f", "7d94b712-5d7f-4e3c-8b58-e383057cf5c9"));
        }

        if (AdsHelperClass.getIronSourceAdStatus() == 1) {
            String adUnitId;

            if (BuildConfig.DEBUG)
                adUnitId = activity.getString(R.string.ads_iron_app_key);
            else {
                adUnitId = AdsHelperClass.getironappkey();
            }
            if (TextUtils.isEmpty(adUnitId)) {
                return;
            }
            IronSource.init(activity, adUnitId);
            IronSource.getAdvertiserId(activity);
            //Network Connectivity Status
            IronSource.shouldTrackNetworkState(activity, true);
        }

        if (AdsHelperClass.getAdmobAdStatus() == 1 || AdsHelperClass.getAdXAdStatus() == 1) {
            if (AdsHelperClass.getIsGDPROn() == 1) {
//                Toast.makeText(activity,  "getIsGDPROn on", Toast.LENGTH_SHORT).show();
            /*ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(activity)
                    .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                    .addTestDeviceHashedId("1E955FCEE279A309514E1F96C8C0239A")
                    .addTestDeviceHashedId("A6CDEA35542F3A2E16B2F1A185A44048")
                    .build();*/


                consentInformation = UserMessagingPlatform.getConsentInformation(activity);

                // Check if you can initialize the Google Mobile Ads SDK in parallel
                // while checking for new consent information. Consent obtained in
                // the previous session can be used to request ads.

                    ConsentRequestParameters params = new ConsentRequestParameters
                            .Builder()
                            .setTagForUnderAgeOfConsent(false)
                            .build();
// .setTagForUnderAgeOfConsent(false)
                    consentInformation.requestConsentInfoUpdate(
                            activity,
                            params,
                            (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                                // TODO: Load and show the consent form.
                                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                                        activity,
                                        (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                            if (loadAndShowError != null) {
                                                // Consent gathering failed.
                                                Log.w(TAG, String.format("%s: %s",
                                                        loadAndShowError.getErrorCode(),
                                                        loadAndShowError.getMessage()));
                                                if (AdsHelperClass.getIsGDPROnFailed() == 1) {
                                                    AdmobSdk(listner, testDeviceIds);
                                                }
                                            }

                                            // Consent has been gathered.
                                            if (consentInformation.canRequestAds()) {
                                                initializeMobileAdsSdk(listner, testDeviceIds);
                                            }
                                        }
                                );

                            },
                            (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                                // Consent gathering failed.
                                Log.w(TAG, String.format("%s: %s",
                                        requestConsentError.getErrorCode(),
                                        requestConsentError.getMessage()));
                                if (AdsHelperClass.getIsGDPROnFailed() == 1) {
                                    AdmobSdk(listner, testDeviceIds);
                                }
//
                            });
                if (consentInformation.canRequestAds()) {
                    initializeMobileAdsSdk(listner, testDeviceIds);
                }
            } else {
//                Toast.makeText(activity,  "getIsGDPROn off", Toast.LENGTH_SHORT).show();
                AdmobSdk(listner, testDeviceIds);
            }
        } else {
            listner.onSuccess();
        }

    }

    private void initializeMobileAdsSdk(getAdsDataListner listner, List<String> testDeviceIds) {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        AdmobSdk(listner, testDeviceIds);
    }

    private void AdmobSdk(getAdsDataListner listner, List<String> testDeviceIds) {
//        Toast.makeText(activity,  "Request an ad.", Toast.LENGTH_SHORT).show();

        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                RequestConfiguration configuration =
                        new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
                MobileAds.setRequestConfiguration(configuration);
//                PrintLog(TAG, "onInitializationComplete: admob");
                listner.onSuccess();
            }

        });


    }

    public void getResponseFromPref(getAdsDataListner listner, List<String> testDeviceIds, int cversion) {
        String response1 = AdsHelperClass.getRemoteData();
        if (!response1.isEmpty()) {


            AdsModel appData = parseAppUserListModel(response1);
            AdsHelperClassAdd(appData);


            if (AdsHelperClass.getapp_redirectOtherAppStatus() == 1) {
                String redirectNewPackage = AdsHelperClass.getapp_newPackageName();
                listner.onRedirect(redirectNewPackage);
            } else if (AdsHelperClass.getapp_updateAppDialogStatus() == 1 && checkUpdate(cversion)) {
                listner.onUpdate("https://play.google.com/store/apps/details?id=" + activity.getPackageName());
            } else {
                if (AdsHelperClass.getAdShowStatus() == 0) {
                    listner.onSuccess();
                } else {
                    initAd(listner, testDeviceIds);
                }
            }
        } else {
            AdsHelperClass.setAdShowStatus(0);
            listner.onSuccess();
        }
    }

    private void loadNextInterstitialPlatform(boolean isNeedResetCount) {
        hideDialog();

        if (interstitial_sequence.size() != 0) {
            interstitial_sequence.remove(0);

            if (interstitial_sequence.size() != 0 && interstitial_sequence.get(0) != null) {
                PrintLog(TAG, "loadNextInterstitialPlatform: " + interstitial_sequence);
                displayInterstitialAds(interstitial_sequence.get(0), activity, isNeedResetCount);
            } else {
                PrintLog(TAG, "loadNextInterstitialPlatform: " + interstitial_sequence);
                AdsHelperClass.isShowingFullScreenAd = false;
                interstitialCallBack();
            }

        } else {
            AdsHelperClass.isShowingFullScreenAd = false;
            interstitialCallBack();
        }
    }

    private void loadNextInterstitialPlatformSplash() {
        hideDialog();
        if (interstitial_sequence.size() != 0) {
            interstitial_sequence.remove(0);

            if (interstitial_sequence.size() != 0 && interstitial_sequence.get(0) != null) {
                PrintLog(TAG, "loadNextInterstitialPlatformSplash: " + interstitial_sequence);
                displayInterstitialAdsSplash(interstitial_sequence.get(0), activity);
            } else {
                PrintLog(TAG, "loadNextInterstitialPlatformSplash: " + interstitial_sequence);
                AdsHelperClass.isShowingFullScreenAd = false;
                AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                interstitialCallBack();
            }
        } else {
            AdsHelperClass.isShowingFullScreenAd = false;
            AdsHelperClass.isIsShowingFullScreenAdSplash = false;
            interstitialCallBack();
        }
    }

    private void loadNextPreInterstitialPlatformSplash() {
        hideDialog();
        if (interstitial_sequence.size() != 0) {
            interstitial_sequence.remove(0);

            if (interstitial_sequence.size() != 0 && interstitial_sequence.get(0) != null) {
                PrintLog(TAG, "loadNextInterstitialPlatformSplash: " + interstitial_sequence);
                displayInterstitialAdsSplash(interstitial_sequence.get(0), activity);
            } else {
                PrintLog(TAG, "loadNextInterstitialPlatformSplash: " + interstitial_sequence);
                AdsHelperClass.isShowingFullScreenAd = false;
                AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                interstitialCallBack();
            }
        } else {
            AdsHelperClass.isShowingFullScreenAd = false;
            AdsHelperClass.isIsShowingFullScreenAdSplash = false;
            interstitialCallBack();
        }
    }

    public void showInterstitialAd(Context context, MyInterstitialCallback myCallback, int how_many_clicks) {
        turnInterstitialAd(context, myCallback, how_many_clicks);

    }

    public void showInterstitialAdSplash(Context context, MyInterstitialCallback myCallback) {
        turnInterstitialAdSplash(context, myCallback);

    }

    public void showPreLoadInterstitialAdSplash(Context context, MyInterstitialCallback myCallback) {
        turnPreInterstitialAdSplash(context, myCallback);

    }

    public void turnInterstitialAdSplash(Context context, MyInterstitialCallback myCallback2) {


        intermyCallback = myCallback2;

        if (!hasActiveInternetConnection(activity) || AdsHelperClass.getAdShowStatus() == 0 || AdsHelperClass.getinterAdStatus() == 0 || AdsHelperClass.getIs_splash_on() == 0) {
            if (intermyCallback != null) {
                intermyCallback.callbackCall();
                intermyCallback = null;
            }
            return;
        }


        if (AdsHelperClass.getFirst_ad_hide() == 1 && AdsHelperClass.getInstanceCount() == 1) {
            if (intermyCallback != null) {
                intermyCallback.callbackCall();
                intermyCallback = null;
            }
            return;
        }


        String adPlatformSequence = AdsHelperClass.getSplashAdsSequence();


        interstitial_sequence = new ArrayList<String>();
        if (/*app_howShowAd == 0 && */!adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");

            interstitial_sequence.addAll(Arrays.asList(adSequence));

        }


        if (interstitial_sequence.size() != 0) {
            AdsHelperClass.isIsShowingFullScreenAdSplash = true;
            displayInterstitialAdsSplash(interstitial_sequence.get(0), context);
        } else {
            interstitialCallBack();
        }

    }

    public void turnPreInterstitialAdSplash(Context context, MyInterstitialCallback myCallback2) {

        intermyCallback = myCallback2;


        if (!hasActiveInternetConnection(activity) || AdsHelperClass.getAdShowStatus() == 0 || AdsHelperClass.getinterAdStatus() == 0 || AdsHelperClass.getIs_splash_on() == 0) {
            if (intermyCallback != null) {
                intermyCallback.callbackCall();
                intermyCallback = null;
            }
            return;
        }


        if (AdsHelperClass.getFirst_ad_hide() == 1 && AdsHelperClass.getInstanceCount() == 1) {
            if (intermyCallback != null) {
                intermyCallback.callbackCall();
                intermyCallback = null;
            }
            return;
        }


        String adPlatformSequence = AdsHelperClass.getSplashAdsSequence();


        interstitial_sequence = new ArrayList<String>();
        if (!adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            interstitial_sequence.addAll(Arrays.asList(adSequence));
        } else {
            if (intermyCallback != null) {
                intermyCallback.callbackCall();
                intermyCallback = null;
            }
            return;
        }

        if (interstitial_sequence.size() != 0) {
            loadPreInterstitialAdsSplash(interstitial_sequence.get(0), context);
        } else {
            if (intermyCallback != null) {
                intermyCallback.callbackCall();
                intermyCallback = null;
            }
        }
    }

    private void displayInterstitialAdSplash(String platform, final Context context) {

        if (platform.equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {

            if (mInterstitialAd != null) {
                mInterstitialAd.show((Activity) context);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        PrintLog(TAG, "The ad was dismissed.");

                        AdsHelperClass.isShowingFullScreenAd = false;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                        interstitialCallBack();

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        // Called when fullscreen content failed to show.
                        PrintLog(TAG, "The ad failed to show.");
                        loadNextInterstitialPlatformSplash();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        AdsHelperClass.isShowingFullScreenAd = true;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = true;
                        PrintLog(TAG, "The ad was shown.");

                    }
                });
            } else {
                interstitialCallBack();
            }

        } else if (platform.equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {

            if (mFbInterstitialAd != null) {
                mFbInterstitialAd.show();


            } else {
                interstitialCallBack();
            }

        } else if (platform.equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {

            if (mAdManagerInterstitialAd != null) {
                mAdManagerInterstitialAd.show((Activity) context);

                mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        PrintLog(TAG, "The ad was dismissed.");


                        AdsHelperClass.isShowingFullScreenAd = false;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = false;

                        interstitialCallBack();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        // Called when fullscreen content failed to show.
                        PrintLog(TAG, "The ad failed to show.");
                        loadNextInterstitialPlatformSplash();

                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mAdManagerInterstitialAd = null;
                        AdsHelperClass.isShowingFullScreenAd = true;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = true;
                        PrintLog(TAG, "The ad was shown.");


                    }
                });
            } else {
                interstitialCallBack();
            }

        } else if (platform.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {

            if (appLovin_interstitialAd != null && appLovin_interstitialAd.isReady()) {

                appLovin_interstitialAd.setListener(new MaxAdListener() {
                    @Override
                    public void onAdLoaded(MaxAd ad) {

                        PrintLog(TAG, "Applovin InterstitialAd ===> onAdLoaded");

                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {
                        PrintLog(TAG, "Applovin InterstitialAd ===> onAdDisplayed");

                        appLovin_interstitialAd = null;
                        AdsHelperClass.isShowingFullScreenAd = true;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = true;

                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {
                        PrintLog(TAG, "Applovin InterstitialAd ===> onAdHidden");

                        AdsHelperClass.isShowingFullScreenAd = false;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                        interstitialCallBack();

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {
                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
                        PrintLog(TAG, "Applovin onAdLoadFailed ===> onAdLoadFailed" + error.getMessage());

                        appLovin_interstitialAd = null;
                        loadNextInterstitialPlatformSplash();
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                        PrintLog(TAG, "Applovin onAdDisplayFailed ===> onAdDisplayFailed" + error.getMessage());
                        appLovin_interstitialAd = null;
                        loadNextInterstitialPlatformSplash();


                    }
                });
                appLovin_interstitialAd.showAd();
            } else {
                interstitialCallBack();
            }

        } else if (platform.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
            if (IronSource.isInterstitialReady()) {
                IronSource.setInterstitialListener(new InterstitialListener() {
                    @Override
                    public void onInterstitialAdReady() {
                    }

                    @Override
                    public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                        PrintLog(TAG, "onInterstitialAdLoadFailed: " + ironSourceError.getErrorMessage());
                        loadNextInterstitialPlatformSplash();
                    }

                    @Override
                    public void onInterstitialAdOpened() {
                        AdsHelperClass.isShowingFullScreenAd = true;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = true;

                    }

                    @Override
                    public void onInterstitialAdClosed() {

                        AdsHelperClass.isShowingFullScreenAd = false;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                        interstitialCallBack();

                    }

                    @Override
                    public void onInterstitialAdShowSucceeded() {
                    }

                    @Override
                    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
                        PrintLog(TAG, "onInterstitialAdShowFailed: " + ironSourceError.getErrorMessage());
                        loadNextInterstitialPlatformSplash();
                    }

                    @Override
                    public void onInterstitialAdClicked() {

                    }
                });
                IronSource.showInterstitial();

            } else {
                interstitialCallBack();
            }

        } else {
            interstitialCallBack();
        }
    }

    private void displayInterstitialAd(String platform, final Context context, boolean isNeedResetCount) {

        if (platform.equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {

            if (mInterstitialAd != null) {
                mInterstitialAd.show((Activity) context);
                int count = AdsHelperClass.getFullScreenAdsShowedCount();
                AdsHelperClass.setFullScreenAdsShowedCount(count + 1);

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        PrintLog(TAG, "The ad was dismissed.");
                        AdsHelperClass.isShowingFullScreenAd = false;
                        interstitialCallBack();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        // Called when fullscreen content failed to show.
                        PrintLog(TAG, "The ad failed to show.");
                        loadNextInterstitialPlatform(isNeedResetCount);
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialAd = null;
                        AdsHelperClass.isShowingFullScreenAd = true;
                        PrintLog(TAG, "The ad was shown.");


                    }
                });
            } else {
                interstitialCallBack();
            }

        } else if (platform.equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {

            if (mFbInterstitialAd != null) {

                int count = AdsHelperClass.getFullScreenAdsShowedCount();
                AdsHelperClass.setFullScreenAdsShowedCount(count + 1);
                mFbInterstitialAd.show();

            } else {
                interstitialCallBack();
            }

        } else if (platform.equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {

            if (mAdManagerInterstitialAd != null) {
                mAdManagerInterstitialAd.show((Activity) context);
                int count = AdsHelperClass.getFullScreenAdsShowedCount();
                AdsHelperClass.setFullScreenAdsShowedCount(count + 1);

                mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        PrintLog(TAG, "The ad was dismissed.");


                        AdsHelperClass.isShowingFullScreenAd = false;

                        interstitialCallBack();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        // Called when fullscreen content failed to show.
                        PrintLog(TAG, "The ad failed to show.");

                        loadNextInterstitialPlatform(isNeedResetCount);

                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mAdManagerInterstitialAd = null;
                        AdsHelperClass.isShowingFullScreenAd = true;
                        PrintLog(TAG, "The ad was shown.");

                    }
                });
            } else {
                interstitialCallBack();
            }

        } else if (platform.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {

            if (appLovin_interstitialAd != null && appLovin_interstitialAd.isReady()) {

                appLovin_interstitialAd.setListener(new MaxAdListener() {
                    @Override
                    public void onAdLoaded(MaxAd ad) {
                        PrintLog(TAG, "Applovin InterstitialAd ===> onAdLoaded");
                        hideDialog();

                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {
                        appLovin_interstitialAd = null;
                        AdsHelperClass.isShowingFullScreenAd = true;

                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {
                        AdsHelperClass.isShowingFullScreenAd = false;
                        interstitialCallBack();

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {
                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
                        PrintLog(TAG, "onAdLoadFailed: " + error.getMessage());

                        appLovin_interstitialAd = null;
                        loadNextInterstitialPlatform(isNeedResetCount);


                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                        PrintLog(TAG, "onAdDisplayFailed: " + error.getMessage());

                        appLovin_interstitialAd = null;
                        loadNextInterstitialPlatform(isNeedResetCount);


                    }
                });
                appLovin_interstitialAd.showAd();
                int count = AdsHelperClass.getFullScreenAdsShowedCount();
                AdsHelperClass.setFullScreenAdsShowedCount(count + 1);
            } else {
                interstitialCallBack();
            }

        } else if (platform.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
            if (IronSource.isInterstitialReady()) {
                int count = AdsHelperClass.getFullScreenAdsShowedCount();
                AdsHelperClass.setFullScreenAdsShowedCount(count + 1);
                IronSource.setInterstitialListener(new InterstitialListener() {
                    @Override
                    public void onInterstitialAdReady() {
                    }

                    @Override
                    public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                        PrintLog(TAG, "onInterstitialAdLoadFailed: " + ironSourceError.getErrorMessage());

                        loadNextInterstitialPlatform(isNeedResetCount);


                    }

                    @Override
                    public void onInterstitialAdOpened() {
                        AdsHelperClass.isShowingFullScreenAd = true;

                    }

                    @Override
                    public void onInterstitialAdClosed() {

                        loadNextInterstitialPlatform(isNeedResetCount);

                    }

                    @Override
                    public void onInterstitialAdShowSucceeded() {
                    }

                    @Override
                    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
                        PrintLog(TAG, "onInterstitialAdShowFailed: " + ironSourceError.getErrorMessage());


                        loadNextInterstitialPlatform(isNeedResetCount);


                    }

                    @Override
                    public void onInterstitialAdClicked() {

                    }
                });
                IronSource.showInterstitial();

            } else {
                interstitialCallBack();
            }

        } else {
            interstitialCallBack();
        }
    }

    private void displayPreInterstitialAd(String platform, final Context context, Boolean isNeedResetCount) {

        if (platform.equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {

            if (mInterstitialAdPre != null) {

                mInterstitialAdPre.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        PrintLog(TAG, "The ad was dismissed.");
                        AdsHelperClass.isShowingFullScreenAd = false;
                        interstitialCallBack();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        // Called when fullscreen content failed to show.
                        PrintLog(TAG, "The ad failed to show.");
                        AdsHelperClass.isShowingFullScreenAd = false;
                        interstitialCallBack();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAdPre = null;
                        AdsHelperClass.isShowingFullScreenAd = true;
                        PrintLog(TAG, "The ad was shown.");
                    }
                });
                mInterstitialAdPre.show((Activity) context);
            } else {
                interstitialCallBack();
            }

        } else if (platform.equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {

            if (mFbInterstitialAdPre != null) {
                mFbInterstitialAdPre.show();
            } else {
                interstitialCallBack();
            }

        } else if (platform.equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {

            if (mAdManagerInterstitialAdPre != null) {

                mAdManagerInterstitialAdPre.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        PrintLog(TAG, "The ad was dismissed.");
                        AdsHelperClass.isShowingFullScreenAd = false;
                        interstitialCallBack();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        // Called when fullscreen content failed to show.
                        PrintLog(TAG, "The ad failed to show.");
                        loadNextInterstitialPlatform(isNeedResetCount);
                        AdsHelperClass.isShowingFullScreenAd = false;
                        interstitialCallBack();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mAdManagerInterstitialAdPre = null;
                        AdsHelperClass.isShowingFullScreenAd = true;
                        PrintLog(TAG, "The ad was shown.");
                    }
                });
                mAdManagerInterstitialAdPre.show((Activity) context);
            } else {
                interstitialCallBack();
            }

        } else if (platform.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {

            if (appLovin_interstitialAdPre != null && appLovin_interstitialAdPre.isReady()) {
                appLovin_interstitialAdPre.setListener(new MaxAdListener() {
                    @Override
                    public void onAdLoaded(MaxAd ad) {
                        PrintLog(TAG, "Applovin InterstitialAd ===> onAdLoaded");
                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {
                        appLovin_interstitialAdPre = null;
                        AdsHelperClass.isShowingFullScreenAd = true;

                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {
                        AdsHelperClass.isShowingFullScreenAd = false;
                        interstitialCallBack();

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {
                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
                        PrintLog(TAG, "onAdLoadFailed: " + error.getMessage());
                        appLovin_interstitialAdPre = null;
                        AdsHelperClass.isShowingFullScreenAd = false;
                        interstitialCallBack();
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                        PrintLog(TAG, "onAdDisplayFailed: " + error.getMessage());
                        appLovin_interstitialAdPre = null;
                        AdsHelperClass.isShowingFullScreenAd = false;
                        interstitialCallBack();
                    }
                });
                appLovin_interstitialAdPre.showAd();
            } else {
                interstitialCallBack();
            }

        } else if (platform.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
            if (IronSource.isInterstitialReady()) {
                IronSource.setInterstitialListener(new InterstitialListener() {
                    @Override
                    public void onInterstitialAdReady() {
                    }

                    @Override
                    public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                        PrintLog(TAG, "onInterstitialAdLoadFailed: " + ironSourceError.getErrorMessage());

                        AdsHelperClass.isShowingFullScreenAd = false;
                        interstitialCallBack();
                    }

                    @Override
                    public void onInterstitialAdOpened() {
                        AdsHelperClass.isShowingFullScreenAd = true;

                    }

                    @Override
                    public void onInterstitialAdClosed() {

                        AdsHelperClass.isShowingFullScreenAd = false;
                        interstitialCallBack();
                    }

                    @Override
                    public void onInterstitialAdShowSucceeded() {
                    }

                    @Override
                    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
                        PrintLog(TAG, "onInterstitialAdShowFailed: " + ironSourceError.getErrorMessage());
                        AdsHelperClass.isShowingFullScreenAd = false;
                        interstitialCallBack();
                    }

                    @Override
                    public void onInterstitialAdClicked() {

                    }
                });
                IronSource.showInterstitial();
            } else {
                interstitialCallBack();
            }
        } else {
            interstitialCallBack();
        }
    }

    public void turnInterstitialAd(Context context, MyInterstitialCallback myCallback2, int how_many_clicks) {
        intermyCallback = myCallback2;
        count_click++;
        if (!hasActiveInternetConnection(context) || AdsHelperClass.getAdShowStatus() == 0 || AdsHelperClass.getinterAdStatus() == 0) {
            if (intermyCallback != null) {
                intermyCallback.callbackCall();
                intermyCallback = null;
            }
            return;
        }

        if (AdsHelperClass.getFirst_ad_hide() == 1 && AdsHelperClass.getInstanceCount() == 1) {
            if (intermyCallback != null) {
                intermyCallback.callbackCall();
                intermyCallback = null;
            }
            return;
        }


        int showCount = AdsHelperClass.getFullScreenAdsShowedCount();
        int totalLimit = AdsHelperClass.getFullScreenLimitCount();
        PrintLog(TAG, "Full Screen App Limit Exist  ShowCount: " + showCount + "  totalLimit: " + totalLimit);


        if (showCount >= totalLimit) {
            PrintLog(TAG, "Full Screen App Limit Exist  ShowCount: " + showCount + "  totalLimit: " + totalLimit);

            if (intermyCallback != null) {
                intermyCallback.callbackCall();
                intermyCallback = null;
            }
            return;
        }

        PrintLog("AdsClick: ", "count_click : " + count_click + "  how_many_clicks: " + how_many_clicks);
        PrintLog("AdsClick: ", "interstitialCountMultiplier : " + AdsHelperClass.getInterstitialCountMultiplier());
        PrintLog("AdsClick: ", "showTime : " + showTime);

        int howManyClicksNew = how_many_clicks;
        if (AdsHelperClass.getInterstitialCountMultiplier() == 1 && showTime == 0) {
            howManyClicksNew = how_many_clicks;
        } else if (AdsHelperClass.getInterstitialCountMultiplier() == 0 && showTime == 0) {
            howManyClicksNew = how_many_clicks;
        } else {
            if (showTime > 0 && AdsHelperClass.getInterstitialCountMultiplier() > 0) {
                PrintLog("AdsClick: ", "oldShowcount : " + oldShowcount);
                howManyClicksNew = oldShowcount * AdsHelperClass.getInterstitialCountMultiplier();
            }
        }

        PrintLog("AdsClick: ", "howManyClicksNew : " + howManyClicksNew);


        if ((AdsHelperClass.getinterstitialCount() > AdsHelperClass.getInterstitialFirstClick() && count_click == AdsHelperClass.getInterstitialFirstClick()) && !isFirstTimeShow) {

            loadAndShowFullScreenAds(context, false);
            isFirstTimeShow = true;
        } else if (AdsHelperClass.getinterstitialCount() > AdsHelperClass.getInterstitialFirstClick() && (count_click > 0 && count_click % howManyClicksNew == 0) /*&& isFirstTimeShow*/) {
            showTime++;
            oldShowcount = howManyClicksNew;
            loadAndShowFullScreenAds(context, true);
        } else {
            if (intermyCallback != null) {
                intermyCallback.callbackCall();
                intermyCallback = null;
            }
        }


    }

    private void loadAndShowFullScreenAdsonFirstClick(Context context, int how_many_clicks, boolean isNeedResetCount) {
        //preload things to show
        if (mInterstitialAdPre != null && AdsHelperClass.getAdmobAdStatus() == 1 && count_click == AdsHelperClass.getInterstitialFirstClick()) {
            displayPreInterstitialAd(AdsHelperClass.ADMOB, activity, isNeedResetCount);
        } else if (mAdManagerInterstitialAdPre != null && AdsHelperClass.getAdXAdStatus() == 1 && count_click == AdsHelperClass.getInterstitialFirstClick()) {
            displayPreInterstitialAd(AdsHelperClass.ADX, activity, isNeedResetCount);
        } else if (mFbInterstitialAdPre != null && AdsHelperClass.getFBAdStatus() == 1 && count_click == AdsHelperClass.getInterstitialFirstClick()) {
            displayPreInterstitialAd(AdsHelperClass.FACEBOOK, activity, isNeedResetCount);
        } else if (appLovin_interstitialAdPre != null && AdsHelperClass.getApplovinAdStatus() == 1 && count_click == AdsHelperClass.getInterstitialFirstClick()) {
            displayPreInterstitialAd(AdsHelperClass.APPLOVIN, activity, isNeedResetCount);
        } /*else if (platform.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
            displayPreInterstitialAd(AdsHelperClass.IRON, activity);
        }*/ else {
            if (count_click == AdsHelperClass.getInterstitialFirstClick()) {

                String adPlatformSequence = AdsHelperClass.getInterSequence();


                interstitial_sequence = new ArrayList<String>();
                if (/*app_howShowAd == 0 && */!adPlatformSequence.isEmpty()) {
                    String adSequence[] = adPlatformSequence.split(",");

                    interstitial_sequence.addAll(Arrays.asList(adSequence));

                }


                if (interstitial_sequence.size() != 0) {
                    displayInterstitialAds(interstitial_sequence.get(0), context, isNeedResetCount);
                } else {
                    interstitialCallBack();
                }
            } else {
                if (intermyCallback != null) {
                    intermyCallback.callbackCall();
                    intermyCallback = null;
                }
            }
        }
    }


    private void loadAndShowFullScreenAds(Context context, boolean isNeedResetCount) {
        //preload things to show
        if (mInterstitialAdPre != null && AdsHelperClass.getAdmobAdStatus() == 1) {
            displayPreInterstitialAd(AdsHelperClass.ADMOB, activity, isNeedResetCount);
        } else if (mAdManagerInterstitialAdPre != null && AdsHelperClass.getAdXAdStatus() == 1) {
            displayPreInterstitialAd(AdsHelperClass.ADX, activity, isNeedResetCount);
        } else if (mFbInterstitialAdPre != null && AdsHelperClass.getFBAdStatus() == 1) {
            displayPreInterstitialAd(AdsHelperClass.FACEBOOK, activity, isNeedResetCount);
        } else if (appLovin_interstitialAdPre != null && AdsHelperClass.getApplovinAdStatus() == 1) {
            displayPreInterstitialAd(AdsHelperClass.APPLOVIN, activity, isNeedResetCount);
        } /*else if (platform.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
            displayPreInterstitialAd(AdsHelperClass.IRON, activity);
        }*/ else {

            String adPlatformSequence = AdsHelperClass.getInterSequence();

            interstitial_sequence = new ArrayList<String>();
            if (/*app_howShowAd == 0 && */!adPlatformSequence.isEmpty()) {
                String adSequence[] = adPlatformSequence.split(",");

                interstitial_sequence.addAll(Arrays.asList(adSequence));

            }


            if (interstitial_sequence.size() != 0) {
                displayInterstitialAds(interstitial_sequence.get(0), context, isNeedResetCount);
            } else {
                interstitialCallBack();
            }


        }

    }

    private void showDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.ads_dialog_loading_progress);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        if (!((Activity) context).isFinishing()) {
            dialog.show();

        }
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void resetClickCount(boolean isNeedResetCount) {
        if (isNeedResetCount) {
            count_click = 0;
        }

    }

    private void displayInterstitialAds(String platform, final Context activity, boolean isNeedResetCount) {

        if (platform.equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getAdmobInterId())) {
                return;
            }
            showDialog(activity);
            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(activity, AdsHelperClass.getAdmobInterId(), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    resetClickCount(isNeedResetCount);
                    mInterstitialAd = interstitialAd;
                    PrintLog(TAG, "onAdLoaded");
                    hideDialog();
                    displayInterstitialAd(AdsHelperClass.ADMOB, activity, isNeedResetCount);


                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    PrintLog(TAG, loadAdError.getMessage());
                    mInterstitialAd = null;
                    loadNextInterstitialPlatform(isNeedResetCount);

                }
            });


        } else if (platform.equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getFBInterId())) {
                return;
            }
            showDialog(activity);

            mFbInterstitialAd = new com.facebook.ads.InterstitialAd(activity, AdsHelperClass.getFBInterId());
            mFbInterstitialAd.loadAd(mFbInterstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    mFbInterstitialAd = null;
                    AdsHelperClass.isShowingFullScreenAd = true;
                    PrintLog(TAG, "The ad was shown.");

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    PrintLog(TAG, "The ad was dismissed.");

                    AdsHelperClass.isShowingFullScreenAd = false;

                    interstitialCallBack();

                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    PrintLog(TAG, adError.getErrorMessage());

                    mFbInterstitialAd = null;
                /*    AdsHelperClass.isShowingFullScreenAd = false;
                    hideDialog();
                    interstitialCallBack();*/
                    loadNextInterstitialPlatform(isNeedResetCount);


                }

                @Override
                public void onAdLoaded(Ad ad) {
                    resetClickCount(isNeedResetCount);
                    mFbInterstitialAd = (com.facebook.ads.InterstitialAd) ad;
                    PrintLog(TAG, "onAdLoaded");
                    hideDialog();
                    displayInterstitialAd(AdsHelperClass.FACEBOOK, activity, isNeedResetCount);

                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            }).build());


        } else if (platform.equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getAdxInterId())) {
                return;
            }
            showDialog(activity);

            AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
            AdManagerInterstitialAd.load(activity, AdsHelperClass.getAdxInterId(), adManagerAdRequest,
                    new AdManagerInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                            resetClickCount(isNeedResetCount);
                            mAdManagerInterstitialAd = interstitialAd;
                            hideDialog();
                            displayInterstitialAd(AdsHelperClass.ADX, activity, isNeedResetCount);


                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            PrintLog(TAG, "onAdFailedToLoad: Code: " + loadAdError.getCause() + " Message: "+loadAdError.getMessage());

                            mAdManagerInterstitialAd = null;
                            loadNextInterstitialPlatform(isNeedResetCount);

                        }
                    });


        } else if (platform.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getapplovininter())) {
                return;
            }
            showDialog(activity);

            PrintLog(TAG, "Applovin Load ===> id: " + AdsHelperClass.getapplovininter());

            appLovin_interstitialAd = new MaxInterstitialAd(AdsHelperClass.getapplovininter(), (Activity) activity);
            appLovin_interstitialAd.setListener(new MaxAdListener() {
                @Override
                public void onAdLoaded(MaxAd ad) {
                    resetClickCount(isNeedResetCount);
                    PrintLog(TAG, "Applovin InterstitialAd ===> onAdLoaded");
                    hideDialog();

                    displayInterstitialAd(AdsHelperClass.APPLOVIN, activity, isNeedResetCount);
                }

                @Override
                public void onAdDisplayed(MaxAd ad) {
                    PrintLog(TAG, "Applovin InterstitialAd ===> onAdDisplayed");
                    appLovin_interstitialAd = null;
                    AdsHelperClass.isShowingFullScreenAd = true;

                }

                @Override
                public void onAdHidden(MaxAd ad) {
                    PrintLog(TAG, "Applovin InterstitialAd ===> onAdHidden");
                    AdsHelperClass.isShowingFullScreenAd = false;
                    interstitialCallBack();

                }

                @Override
                public void onAdClicked(MaxAd ad) {
                    PrintLog(TAG, "Applovin InterstitialAd ===> onAdClicked");
                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    PrintLog(TAG, "Applovin InterstitialAd ===> onAdLoadFailed: " + error.getMessage());

                    appLovin_interstitialAd = null;
                    loadNextInterstitialPlatform(isNeedResetCount);


                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    PrintLog(TAG, "Applovin InterstitialAd ===> onAdDisplayFailed: " + error.getMessage());

                    appLovin_interstitialAd = null;

                    loadNextInterstitialPlatform(isNeedResetCount);


                }
            });
            appLovin_interstitialAd.loadAd();
            PrintLog(TAG, "Applovin loadAd");
        } else if (platform.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getironappkey())) {
                return;
            }
            showDialog(activity);

            IronSource.loadInterstitial();
            IronSource.setInterstitialListener(new InterstitialListener() {
                @Override
                public void onInterstitialAdReady() {
                    hideDialog();
                    resetClickCount(isNeedResetCount);
                    displayInterstitialAd(AdsHelperClass.IRON, activity, isNeedResetCount);
                }

                @Override
                public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                    PrintLog(TAG, ironSourceError.getErrorMessage());

                    loadNextInterstitialPlatform(isNeedResetCount);


                }

                @Override
                public void onInterstitialAdOpened() {
                    AdsHelperClass.isShowingFullScreenAd = true;

                }

                @Override
                public void onInterstitialAdClosed() {
                    AdsHelperClass.isShowingFullScreenAd = false;
                    interstitialCallBack();

                }

                @Override
                public void onInterstitialAdShowSucceeded() {

                }

                @Override
                public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
                    PrintLog(TAG, "onInterstitialAdShowFailed: " + ironSourceError.getErrorMessage());


                    loadNextInterstitialPlatform(isNeedResetCount);


                }

                @Override
                public void onInterstitialAdClicked() {

                }
            });


        } else {
            interstitialCallBack();
        }
    }

    private void loadPreInterstitialAdsSplash(String platform, final Context activity) {
        if (platform.equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getAdmobInterId())) {
                interstitialCallBack();
                return;
            }
            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(activity, AdsHelperClass.getAdmobInterId(), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAdPre = interstitialAd;
                    PrintLog(TAG, "onAdLoaded");
                    interstitialCallBack();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    PrintLog(TAG, loadAdError.getMessage());
                    mInterstitialAdPre = null;
                    loadNextPreInterstitialPlatformSplash();
                }
            });


        } else if (platform.equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getFBInterId())) {
                return;
            }

            mFbInterstitialAdPre = new com.facebook.ads.InterstitialAd(activity, AdsHelperClass.getFBInterId());
            mFbInterstitialAdPre.loadAd(mFbInterstitialAdPre.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    mFbInterstitialAdPre = null;
                    PrintLog(TAG, "The ad was shown.");
                    AdsHelperClass.isShowingFullScreenAd = true;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = true;
                    PrintLog(TAG, "The ad was shown.");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    PrintLog(TAG, "The ad was dismissed.");
                    mFbInterstitialAdPre = null;
                    AdsHelperClass.isShowingFullScreenAd = false;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = false;

                    interstitialCallBack();
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    PrintLog(TAG, adError.getErrorMessage());
                    mFbInterstitialAdPre = null;
                    loadNextPreInterstitialPlatformSplash();
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    mFbInterstitialAdPre = (com.facebook.ads.InterstitialAd) ad;
                    PrintLog(TAG, "onAdLoaded");
                    interstitialCallBack();
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            }).build());
        } else if (platform.equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getAdxInterId())) {
                return;
            }
            AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
            AdManagerInterstitialAd.load(activity, AdsHelperClass.getAdxInterId(), adManagerAdRequest,
                    new AdManagerInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                            mAdManagerInterstitialAdPre = interstitialAd;
                            interstitialCallBack();
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            PrintLog(TAG, "onAdFailedToLoad: Code: " + loadAdError.getCause() + " Message: "+loadAdError.getMessage());

                            mAdManagerInterstitialAdPre = null;

                            loadNextPreInterstitialPlatformSplash();

                        }
                    });


        } else if (platform.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getapplovininter())) {
                return;
            }
            appLovin_interstitialAdPre = new MaxInterstitialAd(AdsHelperClass.getapplovininter(), (Activity) activity);
            appLovin_interstitialAdPre.setListener(new MaxAdListener() {
                @Override
                public void onAdLoaded(MaxAd ad) {
                    PrintLog(TAG, "Applovin InterstitialAd ===> onAdLoaded");
                    interstitialCallBack();
                }

                @Override
                public void onAdDisplayed(MaxAd ad) {
                    appLovin_interstitialAdPre = null;
                }

                @Override
                public void onAdHidden(MaxAd ad) {

                }

                @Override
                public void onAdClicked(MaxAd ad) {
                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    PrintLog(TAG, "Applovin onAdLoadFailed ===> onAdLoadFailed" + error.getMessage());
                    appLovin_interstitialAdPre = null;
                    loadNextPreInterstitialPlatformSplash();
                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    PrintLog(TAG, "Applovin onAdDisplayFailed ===> onAdDisplayFailed" + error.getMessage());
                    appLovin_interstitialAd = null;
                    loadNextPreInterstitialPlatformSplash();
                }
            });
            appLovin_interstitialAd.loadAd();


        } else if (platform.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getironappkey())) {
                return;
            }
            IronSource.loadInterstitial();
            IronSource.setInterstitialListener(new InterstitialListener() {
                @Override
                public void onInterstitialAdReady() {
                    interstitialCallBack();
                }

                @Override
                public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                    PrintLog(TAG, "onInterstitialAdLoadFailed: " + ironSourceError.getErrorMessage());
                    loadNextPreInterstitialPlatformSplash();
                }

                @Override
                public void onInterstitialAdOpened() {
                }

                @Override
                public void onInterstitialAdClosed() {
                }

                @Override
                public void onInterstitialAdShowSucceeded() {

                }

                @Override
                public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
                    PrintLog(TAG, "onInterstitialAdShowFailed: " + ironSourceError.getErrorMessage());
                    loadNextPreInterstitialPlatformSplash();

                }

                @Override
                public void onInterstitialAdClicked() {

                }
            });
        }
    }


    private void displayInterstitialAdsSplash(String platform, final Context activity) {
        if (platform.equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getAdmobInterId())) {
                return;
            }
            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(activity, AdsHelperClass.getAdmobInterId(), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;
                    PrintLog(TAG, "onAdLoaded");


                    displayInterstitialAdSplash(AdsHelperClass.ADMOB, activity);


                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    PrintLog(TAG, loadAdError.getMessage());


                    mInterstitialAd = null;
                 /*   AdsHelperClass.isShowingFullScreenAd = false;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                    interstitialCallBack();*/
                    loadNextInterstitialPlatformSplash();
                }
            });


        } else if (platform.equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getFBInterId())) {
                return;
            }

            mFbInterstitialAd = new com.facebook.ads.InterstitialAd(activity, AdsHelperClass.getFBInterId());
            mFbInterstitialAd.loadAd(mFbInterstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    mFbInterstitialAd = null;
                    AdsHelperClass.isShowingFullScreenAd = true;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = true;
                    PrintLog(TAG, "The ad was shown.");


                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    PrintLog(TAG, "The ad was dismissed.");

                    AdsHelperClass.isShowingFullScreenAd = false;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = false;

                    interstitialCallBack();

                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    PrintLog(TAG, adError.getErrorMessage());

                    mFbInterstitialAd = null;
                 /*   AdsHelperClass.isShowingFullScreenAd = false;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                    interstitialCallBack();*/
                    loadNextInterstitialPlatformSplash();


                }

                @Override
                public void onAdLoaded(Ad ad) {
                    mFbInterstitialAd = (com.facebook.ads.InterstitialAd) ad;
                    PrintLog(TAG, "onAdLoaded");

                    displayInterstitialAdSplash(AdsHelperClass.FACEBOOK, activity);
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            }).build());


        } else if (platform.equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getAdxInterId())) {
                return;
            }
            AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
            AdManagerInterstitialAd.load(activity, AdsHelperClass.getAdxInterId(), adManagerAdRequest,
                    new AdManagerInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                            mAdManagerInterstitialAd = interstitialAd;

                            displayInterstitialAdSplash(AdsHelperClass.ADX, activity);

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error

                            PrintLog(TAG, "onAdFailedToLoad: Code: " + loadAdError.getCause() + " Message: "+loadAdError.getMessage());

                            mAdManagerInterstitialAd = null;

                            loadNextInterstitialPlatformSplash();

                        }
                    });


        } else if (platform.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getapplovininter())) {
                return;
            }
            appLovin_interstitialAd = new MaxInterstitialAd(AdsHelperClass.getapplovininter(), (Activity) activity);
            appLovin_interstitialAd.setListener(new MaxAdListener() {
                @Override
                public void onAdLoaded(MaxAd ad) {
                    PrintLog(TAG, "Applovin InterstitialAd ===> onAdLoaded");
                    displayInterstitialAdSplash(AdsHelperClass.APPLOVIN, activity);
                }

                @Override
                public void onAdDisplayed(MaxAd ad) {
                    appLovin_interstitialAd = null;
                    AdsHelperClass.isShowingFullScreenAd = true;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = true;

                }

                @Override
                public void onAdHidden(MaxAd ad) {
                    AdsHelperClass.isShowingFullScreenAd = false;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                    interstitialCallBack();
                }

                @Override
                public void onAdClicked(MaxAd ad) {
                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    PrintLog(TAG, "Applovin onAdLoadFailed ===> onAdLoadFailed" + error.getMessage());
                    appLovin_interstitialAd = null;
                    loadNextInterstitialPlatformSplash();

                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    PrintLog(TAG, "Applovin onAdDisplayFailed ===> onAdDisplayFailed" + error.getMessage());
                    appLovin_interstitialAd = null;
                    loadNextInterstitialPlatformSplash();
                }
            });
            appLovin_interstitialAd.loadAd();


        } else if (platform.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getironappkey())) {
                return;
            }
            IronSource.loadInterstitial();
            IronSource.setInterstitialListener(new InterstitialListener() {
                @Override
                public void onInterstitialAdReady() {

                    displayInterstitialAdSplash(AdsHelperClass.IRON, activity);
                }

                @Override
                public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                    PrintLog(TAG, "onInterstitialAdLoadFailed: " + ironSourceError.getErrorMessage());

                    loadNextInterstitialPlatformSplash();


                }

                @Override
                public void onInterstitialAdOpened() {
                    AdsHelperClass.isShowingFullScreenAd = true;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = true;

                }

                @Override
                public void onInterstitialAdClosed() {
                    AdsHelperClass.isShowingFullScreenAd = false;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                    interstitialCallBack();

                }

                @Override
                public void onInterstitialAdShowSucceeded() {

                }

                @Override
                public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
                    PrintLog(TAG, "onInterstitialAdShowFailed: " + ironSourceError.getErrorMessage());

                    loadNextInterstitialPlatformSplash();

                }

                @Override
                public void onInterstitialAdClicked() {

                }
            });


        } else {
            interstitialCallBack();
        }
    }

    public void interstitialCallBack() {
        if (intermyCallback != null) {
            intermyCallback.callbackCall();
            intermyCallback = null;
        }
    }

    public void showBanner(ViewGroup banner_container) {


        if (!hasActiveInternetConnection(activity)) {
            banner_container.setVisibility(View.GONE);
            return;
        }

        if (AdsHelperClass.getAdShowStatus() == 0 || AdsHelperClass.getbannerAdStatus() == 0) {
            banner_container.setVisibility(View.GONE);
            return;
        }
        setMinmumHeightForBanner(banner_container);
        String adPlatformSequence = AdsHelperClass.getbannerSequence();

        banner_sequence = new ArrayList<String>();
        if (!adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            banner_sequence.addAll(Arrays.asList(adSequence));

        }
        if (banner_sequence.size() != 0) {
            displayBanner(banner_sequence.get(0), banner_container);
        }
    }

    public void showBannerInvisible(ViewGroup banner_container) {


        if (!hasActiveInternetConnection(activity)) {
            banner_container.setVisibility(View.INVISIBLE);
            return;
        }

        if (AdsHelperClass.getAdShowStatus() == 0 || AdsHelperClass.getbannerAdStatus() == 0) {
            banner_container.setVisibility(View.INVISIBLE);
            return;
        }

        setMinmumHeightForBanner(banner_container);

        String adPlatformSequence = AdsHelperClass.getbannerSequence();

        banner_sequence = new ArrayList<String>();
        if (!adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            banner_sequence.addAll(Arrays.asList(adSequence));

        }

        if (banner_sequence.size() != 0) {
            displayBannerInvisible(banner_sequence.get(0), banner_container);
        }

    }

    private void nextBannerPlatform(ViewGroup banner_container) {
        if (banner_sequence.size() != 0) {
            banner_sequence.remove(0);
            if (banner_sequence.size() != 0) {
                displayBanner(banner_sequence.get(0), banner_container);
            }
        }
    }

    public void displayBanner(String platform, ViewGroup banner_container) {
        if (platform.equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {
            showAdmobBanner(banner_container, View.GONE);
        } else if (platform.equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {
            showAdxBanner(banner_container, View.GONE);
        } else if (platform.equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {
            showFBBanner(banner_container, View.GONE);
        } else if (platform.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {
            showAppLovinBanner(banner_container, View.GONE);
        } else if (platform.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
            showIronSourceBanner(banner_container, View.GONE);
        }
    }

    public void displayBannerInvisible(String platform, ViewGroup banner_container) {
        if (platform.equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {
            showAdmobBanner(banner_container, View.INVISIBLE);
        } else if (platform.equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {
            showAdxBanner(banner_container, View.INVISIBLE);
        } else if (platform.equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {
            showFBBanner(banner_container, View.INVISIBLE);
        } else if (platform.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {
            showAppLovinBanner(banner_container, View.INVISIBLE);
        } else if (platform.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
            showIronSourceBanner(banner_container, View.INVISIBLE);
        }
    }

    private void showAdxBanner(ViewGroup banner_container, int visibility) {
        if (AdsHelperClass.getAdxBannerId().isEmpty()) {
            return;
        }
        setMinmumHeightForBanner(banner_container);
        AdManagerAdView adXManagerAdView = new AdManagerAdView(activity);
        adXManagerAdView.setAdUnitId(AdsHelperClass.getAdxBannerId());
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        AdSize adSize = getAdSize(activity);
        adXManagerAdView.setAdSize(adSize);
        adXManagerAdView.loadAd(adRequest);
        adXManagerAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                PrintLog(TAG, "onAdFailedToLoad: AdxBanner " + loadAdError.getMessage());

                banner_container.removeAllViews();
                if (AdsHelperClass.getIsBannerSpaceVisible() == 1) {
                    banner_container.setVisibility(View.INVISIBLE);
                } else {
                    banner_container.setVisibility(visibility);
                }

                nextBannerPlatform(banner_container);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                PrintLog(TAG, "onAdLoaded: AdxBanner");

                banner_container.removeAllViews();

                banner_container.addView(adXManagerAdView);

                banner_container.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                PrintLog(TAG, "onAdImpression: AdxBanner");

            }
        });


    }

    private void showAdmobBanner(final ViewGroup banner_container, int visibility) {
        if (AdsHelperClass.getAdmobBannerId().isEmpty()) {
            banner_container.setVisibility(View.INVISIBLE);
            return;
        }

        setMinmumHeightForBanner(banner_container);
        final AdView mAdView = new AdView(activity);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(AdsHelperClass.getAdmobBannerId());
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                PrintLog(TAG, "onAdFailedToLoad:banner " + loadAdError.getMessage());


                banner_container.removeAllViews();
                if (AdsHelperClass.getIsBannerSpaceVisible() == 1) {
                    banner_container.setVisibility(View.INVISIBLE);
                } else {
                    banner_container.setVisibility(visibility);
                }
                nextBannerPlatform(banner_container);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                banner_container.setVisibility(View.VISIBLE);
                banner_container.removeAllViews();
                banner_container.addView(mAdView);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                PrintLog(TAG, "onAdImpression: AdmobBanner");

            }
        });

    }

    private void showFBBanner(final ViewGroup banner_container, int visibility) {
        if (AdsHelperClass.getFBBannerId().isEmpty()) {
            return;
        }
        setMinmumHeightForBanner(banner_container);

        final com.facebook.ads.AdView mAdView = new com.facebook.ads.AdView(activity, AdsHelperClass.getFBBannerId(), com.facebook.ads.AdSize.BANNER_HEIGHT_50);

        mAdView.loadAd(mAdView.buildLoadAdConfig().withAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                PrintLog(TAG, "onError:FBBanner " + adError.getErrorMessage());

                banner_container.removeAllViews();
                if (AdsHelperClass.getIsBannerSpaceVisible() == 1) {
                    banner_container.setVisibility(View.INVISIBLE);
                } else {
                    banner_container.setVisibility(visibility);
                }

                nextBannerPlatform(banner_container);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                banner_container.removeAllViews();
                banner_container.setVisibility(View.VISIBLE);
                banner_container.addView(mAdView);


            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {
                PrintLog(TAG, "onAdImpression: FBBanner");


            }
        }).build());
    }

    private void showAppLovinBanner(final ViewGroup banner_container, int visibility) {
        if (AdsHelperClass.getapplovinbanner().isEmpty()) {
            return;
        }
        setMinmumHeightForBanner(banner_container);

        MaxAdView adView = new MaxAdView(AdsHelperClass.getapplovinbanner(), activity);
        adView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {

            }

            @Override
            public void onAdCollapsed(MaxAd ad) {

            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                banner_container.removeAllViews();
                banner_container.setVisibility(View.VISIBLE);
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = activity.getResources().getDimensionPixelSize(R.dimen.banner_height);
                adView.setLayoutParams(new FrameLayout.LayoutParams(width, height, Gravity.BOTTOM));
                adView.setBackgroundColor(Color.WHITE);
                banner_container.addView(adView);
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                PrintLog(TAG, "onAdLoadFailed:banner " + error.getMessage());
                banner_container.removeAllViews();
                if (AdsHelperClass.getIsBannerSpaceVisible() == 1) {
                    banner_container.setVisibility(View.INVISIBLE);
                } else {
                    banner_container.setVisibility(visibility);
                }
                nextBannerPlatform(banner_container);


            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                PrintLog(TAG, "onAdDisplayFailed:banner " + error.getMessage());


                banner_container.removeAllViews();
                banner_container.setVisibility(View.GONE);
                nextBannerPlatform(banner_container);
            }
        });


        adView.loadAd();

    }

    public void setMinmumHeightForBanner(ViewGroup banner_container) {
        if (AdsHelperClass.getIsBannerSpaceVisible() == 1) {
            int val = (int) MyApplicationAds.getContext().getResources().getDimension(R.dimen.margin_55);
            banner_container.setMinimumHeight(val);
            banner_container.setVisibility(View.VISIBLE);
        }
    }

    private void showIronSourceBanner(ViewGroup banner_container, int visibility) {
        if (AdsHelperClass.getironappkey().isEmpty()) {
            return;
        }
        setMinmumHeightForBanner(banner_container);
        ISBannerSize size = ISBannerSize.BANNER;
        mIronSourceBannerLayout = IronSource.createBanner(activity, size);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mIronSourceBannerLayout.setBannerListener(new BannerListener() {
            @Override
            public void onBannerAdLoaded() {
                PrintLog(TAG, "onBannerAdLoaded: ");
                // since banner container was "gone" by default, we need to make it visible as soon as the banner is ready
                banner_container.removeAllViews();
                banner_container.setVisibility(View.VISIBLE);
                banner_container.addView(mIronSourceBannerLayout, 0, layoutParams);


            }

            @Override
            public void onBannerAdLoadFailed(IronSourceError error) {
                PrintLog(TAG, "onBannerAdLoadFailed: ");


                banner_container.removeAllViews();
                if (AdsHelperClass.getIsBannerSpaceVisible() == 1) {
                    banner_container.setVisibility(View.INVISIBLE);
                } else {
                    banner_container.setVisibility(visibility);
                }
                nextBannerPlatform(banner_container);

            }

            @Override
            public void onBannerAdClicked() {
            }

            @Override
            public void onBannerAdScreenPresented() {
            }

            @Override
            public void onBannerAdScreenDismissed() {
            }

            @Override
            public void onBannerAdLeftApplication() {
            }
        });

        // load ad into the created banner
        IronSource.loadBanner(mIronSourceBannerLayout);
    }

    public void PauseIronSourceBanner(Activity activity) {
        if (AdsHelperClass.getbannerSequence().equalsIgnoreCase(AdsHelperClass.IRON)) {
            if (mIronSourceBannerLayout != null) {
                IronSource.onPause(activity);
            }
        }
    }

    public void PreLoadNative(int nativeViewType) {

        if (!hasActiveInternetConnection(activity)) {

            return;
        }

        if (AdsHelperClass.getAdShowStatus() == 0 || AdsHelperClass.getnativeAdStatus() == 0) {

            return;
        }

        String adPlatformSequence = AdsHelperClass.getnativeSequence();
        native_sequence = new ArrayList<String>();
        if (!adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            for (int i = 0; i < adSequence.length; i++) {
                native_sequence.add(adSequence[i]);
            }

        }


        if (native_sequence.size() != 0) {
            LoadPreloadNative(native_sequence.get(0), nativeViewType);
        }


    }

    private void LoadPreloadNative(String s, int nativeViewType) {
        if (s.equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {

            if (AdsHelperClass.getAdmobNativeId().isEmpty()) {
                return;
            }

            if (preAdmobNative == null) {
                final AdLoader adLoader = new AdLoader.Builder(activity, AdsHelperClass.getAdmobNativeId())
                        .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(NativeAd nativeAd) {
                                // Show the ad.
                                preAdmobNative = nativeAd;

                            }
                        })
                        .withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(LoadAdError adError) {
                                PrintLog(TAG, "onAdFailedToLoad:native " + adError.getMessage());
                                nextPreNativePlatform(nativeViewType);

                            }
                        })

                        .build();

                adLoader.loadAd(new AdRequest.Builder().build());
            } else {
                PrintLog(TAG, "PreLoadNative: AlreadyLoaded");
            }
        } else if (s.equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {

            if (AdsHelperClass.getAdxNativeId().isEmpty()) {
                return;
            }

            if (preAdxNative == null) {
                 AdLoader adLoader = new AdLoader.Builder(activity, AdsHelperClass.getAdxNativeId())
                        .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(NativeAd nativeAd) {
                                // Show the ad.
                                PrintLog(TAG, "onNativeAdLoaded: Adx");
                                preAdxNative = nativeAd;
                            }
                        })
                        .withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(LoadAdError adError) {
                                // Handle the failure by logging, altering the UI, and so on.
                                PrintLog(TAG, "onAdFailedToLoad: Adx" + adError.getMessage());
                                nextPreNativePlatform(/*isNativeSmall,*/nativeViewType);
                            }
                        })
                        .build();

                adLoader.loadAd(new AdRequest.Builder().build());
            } else {
                PrintLog(TAG, "PreLoadNative: AlreadyLoaded");
            }
        } else if (s.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {

            if (AdsHelperClass.getapplovinnative().isEmpty()) {
                return;
            }

            if (preMaxNativeAdView == null) {

                MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(AdsHelperClass.getapplovinnative(), activity);
                nativeAdLoader.loadAd(createNativeAdView(activity));
                nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                    @Override
                    public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {

                        PrintLog(TAG, "onNativeAdLoaded: ");
                        preMaxNativeAdView = nativeAdView;

                    }

                    @Override
                    public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {

                        PrintLog(TAG, "onNativeAdLoadFailed: ");
                        nextPreNativePlatform(nativeViewType);

                    }

                    @Override
                    public void onNativeAdClicked(final MaxAd ad) {
                        // Optional click callback
                    }
                });
            } else {
                PrintLog(TAG, "PreLoadNative: AlreadyLoaded");
            }
        } else if (s.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {

            if (AdsHelperClass.getironappkey().isEmpty()) {
                return;
            }
            if (preIronSourceNativeLayout == null) {

                ISBannerSize size = ISBannerSize.RECTANGLE;

                preIronSourceNativeLayout = IronSource.createBanner(activity, size);

                // add IronSourceBanner to your container


                // set the banner listener
                preIronSourceNativeLayout.setBannerListener(new BannerListener() {
                    @Override
                    public void onBannerAdLoaded() {
                        // since banner container was "gone" by default, we need to make it visible as soon as the banner is ready
                        isIronNativePreloaded = true;
                    }

                    @Override
                    public void onBannerAdLoadFailed(IronSourceError error) {
                        PrintLog(TAG, "onAdFailedToLoad:native " + error.getErrorMessage());
                        nextPreNativePlatform(nativeViewType);
                        isIronNativePreloaded = false;

                    }

                    @Override
                    public void onBannerAdClicked() {
                    }

                    @Override
                    public void onBannerAdScreenPresented() {
                    }

                    @Override
                    public void onBannerAdScreenDismissed() {
                    }

                    @Override
                    public void onBannerAdLeftApplication() {
                    }
                });
                // load ad into the created banner
                IronSource.loadBanner(preIronSourceNativeLayout);


            } else {
                PrintLog(TAG, "PreLoadNative: AlreadyLoaded");
            }
        } else if (s.equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {

            if (AdsHelperClass.getFBNativeId().isEmpty()) {
                return;
            }
            if (preFbNative == null) {
                final com.facebook.ads.NativeAd nativeAd1 = new com.facebook.ads.NativeAd(activity, AdsHelperClass.getFBNativeId());
                nativeAd1.loadAd(nativeAd1.buildLoadAdConfig()
                        .withMediaCacheFlag(NativeAdBase.MediaCacheFlag.ALL)/*Pre-cache all (icon, images, and video), default*/
                        .withAdListener(new NativeAdListener() {
                            @Override
                            public void onMediaDownloaded(Ad ad) {

                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                PrintLog(TAG, "onError:FBNative " + adError.getErrorMessage());

                                nextPreNativePlatform(nativeViewType);
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                PrintLog(TAG, "onAdLoaded: ");
                                preFbNative = nativeAd1;


                            }

                            @Override
                            public void onAdClicked(Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {

                            }
                        })
                        .build());
            } else {
                PrintLog(TAG, "PreLoadNative: AlreadyLoaded");
            }
        }
    }
    public void showNativeAdapter(ViewGroup nativeAdContainer, boolean isNeedSpace, boolean isNativeInRecyclerview, int nativeViewType) {
        if (!hasActiveInternetConnection(activity)) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        if (AdsHelperClass.getAdShowStatus() == 0 || AdsHelperClass.getnativeAdStatus() == 0) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        if (isNeedSpace) {
            final float scale = activity.getResources().getDisplayMetrics().density;
            int pixels = (int) (nativeAdSize * scale + 0.5f);

            nativeAdContainer.getLayoutParams().height = pixels;
            nativeAdContainer.requestLayout();
        }

        if (isNativeInRecyclerview) {
            //preload
            preLoadNativeAdapter(nativeAdContainer, isNeedSpace, nativeViewType);
        } else {
            if (AdsHelperClass.getIsOnLoadNative() == 1) {
                onLoadshowNative(nativeAdContainer, isNeedSpace, nativeViewType);
            } else {
                //preload
                preLoadNativeAdapter(nativeAdContainer, isNeedSpace, nativeViewType);
            }
        }
    }

    public void preLoadNativeAdapter(ViewGroup nativeAdContainer, boolean isNeedSpace,/*boolean isNativeSmall,*/int nativeViewType) {
        if (!hasActiveInternetConnection(activity)) {
            return;
        }

        if (AdsHelperClass.getAdShowStatus() == 0 || AdsHelperClass.getnativeAdStatus() == 0) {
            return;
        }
        String adPlatformSequence = AdsHelperClass.getnativeSequence();
        native_sequence = new ArrayList<String>();
        if (!adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            for (int i = 0; i < adSequence.length; i++) {
                native_sequence.add(adSequence[i]);
            }

        }
        if (native_sequence.size() != 0) {
            if (preAdmobNative != null && native_sequence.get(0).equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {
                PrintLog(TAG, "PreLoadNative: ShowLoaded");
                new Inflate_ADS(activity).inflate_NATIV_ADMOB(preAdmobNative, nativeAdContainer, isNeedSpace,/*isNativeSmall,*/nativeViewType);
            } else if (preAdxNative != null && native_sequence.get(0).equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {
                PrintLog(TAG, "PreLoadNative: ShowLoaded");
                new Inflate_ADS(activity).inflate_NATIV_ADMOB(preAdxNative, nativeAdContainer, isNeedSpace,/*isNativeSmall,*/nativeViewType);
            } else if (preFbNative != null && native_sequence.get(0).equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {
                PrintLog(TAG, "PreLoadNative: ShowLoaded");
                new Inflate_ADS(activity).inflate_NATIV_FB(preFbNative, nativeAdContainer, isNeedSpace, nativeViewType);

            } else if (preIronSourceNativeLayout != null && native_sequence.get(0).equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
                PrintLog(TAG, "PreLoadNative: ShowLoaded");
                if (isIronNativePreloaded) {
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT);
                    nativeAdContainer.removeAllViews();
                    try {
                        nativeAdContainer.addView(preIronSourceNativeLayout, 0, layoutParams);
                    } catch (Exception e) {
                        if (preIronSourceNativeLayout.getParent() != null) {
                            ((ViewGroup) preIronSourceNativeLayout.getParent()).removeView(preIronSourceNativeLayout); // <- fix
                        }
                        nativeAdContainer.addView(preIronSourceNativeLayout);
                        PrintLog(TAG, "PreLoadNative: Applovin " + e.getMessage());
                    }
                    nativeAdContainer.setVisibility(View.VISIBLE);
                }

            } else if (preMaxNativeAdView != null && native_sequence.get(0).equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {
                PrintLog(TAG, "PreLoadNative: Applovin ShowLoaded");

                nativeAdContainer.setVisibility(View.VISIBLE);
                // Add ad view to view.
                nativeAdContainer.removeAllViews();

                try {
                    nativeAdContainer.addView(preMaxNativeAdView);
                } catch (Exception e) {
                    if (preMaxNativeAdView.getParent() != null) {
                        ((ViewGroup) preMaxNativeAdView.getParent()).removeView(preMaxNativeAdView); // <- fix
                    }
                    nativeAdContainer.addView(preMaxNativeAdView);
                    PrintLog(TAG, "PreLoadNative: Applovin " + e.getMessage());
                }

            } else {
                PrintLog(TAG, "PreLoadNative: NotLoaded");
                PreLoadNative(nativeViewType);
            }
        }
    }



    public void showNative(ViewGroup nativeAdContainer, boolean isNeedSpace, boolean isNativeInRecyclerview, int nativeViewType) {
        if (!hasActiveInternetConnection(activity)) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        if (AdsHelperClass.getAdShowStatus() == 0 || AdsHelperClass.getnativeAdStatus() == 0) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        if (isNativeInRecyclerview) {
            //preload
            preLoadNative(nativeAdContainer, isNeedSpace, nativeViewType,false);
        } else {
            if (AdsHelperClass.getIsOnLoadNative() == 1) {
                onLoadshowNative(nativeAdContainer, isNeedSpace, nativeViewType);
            } else {
                //preload
                preLoadNative(nativeAdContainer, isNeedSpace, nativeViewType,false);
            }
        }
    }

    public void preLoadNative(ViewGroup nativeAdContainer, boolean isNeedSpace,/*boolean isNativeSmall,*/int nativeViewType, boolean isFromActivity) {

        if(isFromActivity){
            if (!hasActiveInternetConnection(activity)) {
                nativeAdContainer.setVisibility(View.GONE);
                return;
            }

            if (AdsHelperClass.getAdShowStatus() == 0 || AdsHelperClass.getnativeAdStatus() == 0) {
                nativeAdContainer.setVisibility(View.GONE);
                return;
            }
            String adPlatformSequence = AdsHelperClass.getnativeSequence();
            native_sequence = new ArrayList<String>();
            if (!adPlatformSequence.isEmpty()) {
                String adSequence[] = adPlatformSequence.split(",");
                for (int i = 0; i < adSequence.length; i++) {
                    native_sequence.add(adSequence[i]);
                }

            }
        }

        if (native_sequence.size() != 0) {
            if (preAdmobNative != null && native_sequence.get(0).equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {
                PrintLog(TAG, "PreLoadNative: ShowLoaded");
                new Inflate_ADS(activity).inflate_NATIV_ADMOB(preAdmobNative, nativeAdContainer, isNeedSpace,/*isNativeSmall,*/nativeViewType);
            } else if (preAdxNative != null && native_sequence.get(0).equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {
                PrintLog(TAG, "PreLoadNative: ShowLoaded");
                new Inflate_ADS(activity).inflate_NATIV_ADMOB(preAdxNative, nativeAdContainer, isNeedSpace,/*isNativeSmall,*/nativeViewType);
            } else if (preFbNative != null && native_sequence.get(0).equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {
                PrintLog(TAG, "PreLoadNative: ShowLoaded");
                new Inflate_ADS(activity).inflate_NATIV_FB(preFbNative, nativeAdContainer, isNeedSpace, nativeViewType);

            } else if (preIronSourceNativeLayout != null && native_sequence.get(0).equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
                PrintLog(TAG, "PreLoadNative: ShowLoaded");
                if (isIronNativePreloaded) {
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT);
                    nativeAdContainer.removeAllViews();
                    try {
                        nativeAdContainer.addView(preIronSourceNativeLayout, 0, layoutParams);
                    } catch (Exception e) {
                        if (preIronSourceNativeLayout.getParent() != null) {
                            ((ViewGroup) preIronSourceNativeLayout.getParent()).removeView(preIronSourceNativeLayout); // <- fix
                        }
                        nativeAdContainer.addView(preIronSourceNativeLayout);
                        PrintLog(TAG, "PreLoadNative: Applovin " + e.getMessage());
                    }
                    nativeAdContainer.setVisibility(View.VISIBLE);
                }

            } else if (preMaxNativeAdView != null && native_sequence.get(0).equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {
                PrintLog(TAG, "PreLoadNative: Applovin ShowLoaded");

                nativeAdContainer.setVisibility(View.VISIBLE);
                // Add ad view to view.
                nativeAdContainer.removeAllViews();

                try {
                    nativeAdContainer.addView(preMaxNativeAdView);
                } catch (Exception e) {
                    if (preMaxNativeAdView.getParent() != null) {
                        ((ViewGroup) preMaxNativeAdView.getParent()).removeView(preMaxNativeAdView); // <- fix
                    }
                    nativeAdContainer.addView(preMaxNativeAdView);
                    PrintLog(TAG, "PreLoadNative: Applovin " + e.getMessage());
                }

            } else {
                PrintLog(TAG, "PreLoadNative: NotLoaded");
                PreLoadNative(nativeViewType);
            }
        }
    }


    private void nextPreNativePlatform(/*boolean isNativeSmall,*/ int nativeViewType) {
        if (native_sequence.size() != 0) {
            native_sequence.remove(0);
            if (native_sequence.size() != 0) {
                LoadPreloadNative(native_sequence.get(0), nativeViewType);
            }
        }
    }

    private void nextNativePlatform(ViewGroup nativeAdContainer, boolean isNeedSpace/*,boolean isNativeSmall*/, int nativeViewType) {
        if (native_sequence.size() != 0) {
            native_sequence.remove(0);
            if (native_sequence.size() != 0) {
                displayNative(native_sequence.get(0), nativeAdContainer, isNeedSpace, nativeViewType);
            }
        }
    }

    public void onLoadshowNative(ViewGroup nativeAdContainer, boolean isNeedSpace/*,boolean isNativeSmall*/, int nativeViewType) {

        if (!hasActiveInternetConnection(activity)) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        if (AdsHelperClass.getAdShowStatus() == 0 || AdsHelperClass.getnativeAdStatus() == 0) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }


        String adPlatformSequence = AdsHelperClass.getnativeSequence();


        native_sequence = new ArrayList<String>();
        if (!adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            for (int i = 0; i < adSequence.length; i++) {
                native_sequence.add(adSequence[i]);
            }
        }
        if (native_sequence.size() != 0) {
            displayNative(native_sequence.get(0), nativeAdContainer, isNeedSpace, nativeViewType);
        }

    }

    public void displayNative(String platform, ViewGroup nativeAdContainer, boolean isNeedSpace/*,boolean isNativeSmall*/, int nativeViewType) {
        if (platform.equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {
            showAdmobNative(nativeAdContainer, isNeedSpace, nativeViewType);
        } else if (platform.equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {
            showAdxNative(nativeAdContainer, isNeedSpace, nativeViewType);
        } else if (platform.equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {
            showFBNative(nativeAdContainer, isNeedSpace, nativeViewType);
        } else if (platform.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {
            showAppLovinNativeCustom(nativeAdContainer, isNeedSpace, nativeViewType);
        } else if (platform.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
            showIronNative(nativeAdContainer, isNeedSpace, nativeViewType);
        }
    }

    private void showAdmobNative(final ViewGroup nativeAdContainer, boolean isNeedSpace/*,boolean isNativeSmall*/, int nativeViewType) {
        String nativeID = "";

        /*if(isNativeSmall){
            nativeID = AdsHelperClass.getAdmobSmallNativeId();
        }else {*/
        nativeID = AdsHelperClass.getAdmobNativeId();
//        }
        if (nativeID.isEmpty()) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        final AdLoader adLoader = new AdLoader.Builder(activity, nativeID)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        // Show the ad.
                        new Inflate_ADS(activity).inflate_NATIV_ADMOB(nativeAd, nativeAdContainer, isNeedSpace,/*isNativeSmall,*/nativeViewType);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        PrintLog(TAG, "onAdFailedToLoad:native " + adError.getMessage());

                        // Handle the failure by logging, altering the UI, and so on.
                        nativeAdContainer.removeAllViews();
                        nativeAdContainer.setVisibility(View.GONE);
                        nextNativePlatform(nativeAdContainer, isNeedSpace, nativeViewType);

                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }

    private void showFBNative(final ViewGroup nativeAdContainer, boolean isNeedSpace/*,boolean isNativeSmall*/, int nativeViewType) {

        String nativeID = "";

       /* if(isNativeSmall){
            nativeID = AdsHelperClass.getFBSmallNativeId();
        }else {*/
        nativeID = AdsHelperClass.getFBNativeId();
//        }


        if (nativeID.isEmpty()) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        final com.facebook.ads.NativeAd nativeAd1 = new com.facebook.ads.NativeAd(activity, nativeID);
        nativeAd1.loadAd(nativeAd1.buildLoadAdConfig()
                .withMediaCacheFlag(NativeAdBase.MediaCacheFlag.ALL)/*Pre-cache all (icon, images, and video), default*/
                .withAdListener(new NativeAdListener() {
                    @Override
                    public void onMediaDownloaded(Ad ad) {

                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        PrintLog(TAG, "onError:FBNative " + adError.getErrorMessage());

                        nativeAdContainer.removeAllViews();
                        nativeAdContainer.setVisibility(View.GONE);
                        nextNativePlatform(nativeAdContainer, isNeedSpace, nativeViewType);

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        PrintLog(TAG, "onAdLoaded: ");

                        new Inflate_ADS(activity).inflate_NATIV_FB(nativeAd1, nativeAdContainer, isNeedSpace, nativeViewType);

                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                })
                .build());
    }

    private void showAdxNative(final ViewGroup nativeAdContainer, boolean isNeedSpace/*,boolean isNativeSmall*/, int nativeViewType) {

        String nativeID = "";

        /*if(isNativeSmall){
            nativeID = AdsHelperClass.getAdxSmallNativeId();
        }else {*/
        nativeID = AdsHelperClass.getAdxNativeId();
//        }

        if (nativeID.isEmpty()) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        final AdLoader adLoader = new AdLoader.Builder(activity, nativeID)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        // Show the ad.
                        PrintLog(TAG, "onNativeAdLoaded: Adx");


                        new Inflate_ADS(activity).inflate_NATIV_ADMOB(nativeAd, nativeAdContainer, isNeedSpace,/*isNativeSmall,*/nativeViewType);


                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        PrintLog(TAG, "onAdFailedToLoad: Adx" + adError.getMessage());
                        nativeAdContainer.removeAllViews();
                        nativeAdContainer.setVisibility(View.GONE);
                        nextNativePlatform(nativeAdContainer, isNeedSpace, nativeViewType);

                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }


    private void showAppLovinNativeCustom(final ViewGroup nativeAdContainer, boolean isNeedSpace/*,boolean isNativeSmall*/, int nativeViewType) {
        if (AdsHelperClass.getapplovinnative().isEmpty()) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }
        MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(AdsHelperClass.getapplovinnative(), activity);
        nativeAdLoader.loadAd(createNativeAdView(activity));
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {

                PrintLog(TAG, "onNativeAdLoaded: ");

                nativeAdContainer.setVisibility(View.VISIBLE);

                // Clean up any pre-existing native ad to prevent memory leaks.
                if (nativeAd2 != null) {
                    nativeAdLoader.destroy(nativeAd2);
                }

                // Save ad for cleanup.
                nativeAd2 = ad;
                // Add ad view to view.
                nativeAdContainer.removeAllViews();

                nativeAdContainer.addView(nativeAdView);
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {

                PrintLog(TAG, "onNativeAdLoadFailed: ");

                nativeAdContainer.removeAllViews();
                nativeAdContainer.setVisibility(View.GONE);
                // We recommend retrying with exponentially higher delays up to a maximum delay
                nextNativePlatform(nativeAdContainer, isNeedSpace, nativeViewType);

            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {
                // Optional click callback
            }
        });

    }

    private void showIronNative(ViewGroup nativeAdContainer, boolean isNeedSpace/*, boolean isNativeSmall*/, int nativeViewType) {
        if (AdsHelperClass.getironappkey().isEmpty()) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }
        ISBannerSize size = ISBannerSize.RECTANGLE;

        mIronSourceNativeLayout = IronSource.createBanner(activity, size);

        // add IronSourceBanner to your container
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        nativeAdContainer.removeAllViews();

        nativeAdContainer.addView(mIronSourceNativeLayout, 0, layoutParams);

        // set the banner listener
        mIronSourceNativeLayout.setBannerListener(new BannerListener() {
            @Override
            public void onBannerAdLoaded() {
                // since banner container was "gone" by default, we need to make it visible as soon as the banner is ready

                nativeAdContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBannerAdLoadFailed(IronSourceError error) {
                PrintLog(TAG, "onAdFailedToLoad:native " + error.getErrorMessage());

                nativeAdContainer.removeAllViews();
                nativeAdContainer.setVisibility(View.GONE);
                nextNativePlatform(nativeAdContainer, isNeedSpace, nativeViewType);

            }

            @Override
            public void onBannerAdClicked() {
            }

            @Override
            public void onBannerAdScreenPresented() {
            }

            @Override
            public void onBannerAdScreenDismissed() {
            }

            @Override
            public void onBannerAdLeftApplication() {
            }
        });

        // load ad into the created banner
        IronSource.loadBanner(mIronSourceNativeLayout);
    }

    public void PauseIronSourceNativePre(Activity activity) {
        if (AdsHelperClass.getbannerSequence().equalsIgnoreCase(AdsHelperClass.IRON)) {
            if (preIronSourceNativeLayout != null) {
                IronSource.onPause(activity);
            }
        }
    }

    public void ResumeIronSourceNativePre(FrameLayout banner_container, Activity activity, boolean isNeedSpace/*,boolean isNativeSmall*/, int nativeViewType) {
        if (AdsHelperClass.getbannerSequence().equalsIgnoreCase(AdsHelperClass.IRON)) {
            if (preIronSourceNativeLayout != null) {
                IronSource.destroyBanner(preIronSourceNativeLayout);
                showIronNative(banner_container, isNeedSpace, nativeViewType);
                IronSource.onResume(activity);
            }
        }

    }

    public void PauseIronSourceNative(Activity activity) {
        if (AdsHelperClass.getbannerSequence().equalsIgnoreCase(AdsHelperClass.IRON)) {
            if (mIronSourceNativeLayout != null) {
                IronSource.onPause(activity);
            }
        }
    }

    public void ResumeIronSourceNative(FrameLayout banner_container, Activity activity, boolean isNeedSpace/*,boolean isNativeSmall*/, int nativeViewType) {
        if (AdsHelperClass.getbannerSequence().equalsIgnoreCase(AdsHelperClass.IRON)) {
            if (mIronSourceNativeLayout != null) {
                IronSource.destroyBanner(mIronSourceNativeLayout);
                showIronNative(banner_container, isNeedSpace, nativeViewType);
                IronSource.onResume(activity);
            }
        }

    }

    public interface MyInterstitialCallback {
        void callbackCall();

    }


}
