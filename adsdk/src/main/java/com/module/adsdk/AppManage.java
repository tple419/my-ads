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


public class AppManage {
    private static final String TAG = "AppManage";
    public static int count_click = -1;
    public static SharedPreferences mysharedpreferences;
    public static MaxAd nativeAd2;
    static Activity activity;
    static MyInterstitialCallback intermyCallback;
    private static AppManage mInstance;
    public AdManagerInterstitialAd mAdManagerInterstitialAd;
    ArrayList<String> banner_sequence = new ArrayList<>();
    ArrayList<String> native_sequence = new ArrayList<>();
    ArrayList<String> interstitial_sequence = new ArrayList<>();
    private InterstitialAd mInterstitialAd;
    private com.facebook.ads.InterstitialAd mFbInterstitialAd;
    private MaxInterstitialAd appLovin_interstitialAd;
    private Dialog dialog;
    IronSourceBannerLayout mIronSourceBannerLayout;
    IronSourceBannerLayout mIronSourceNativeLayout;
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
        AdsHelperClass.setAppOpenSequence(appData.getAppOpen_sequence());
        AdsHelperClass.setapp_newPackageName(appData.getApp_newPackageName());
        AdsHelperClass.setapp_redirectOtherAppStatus(appData.getApp_redirectOtherAppStatus());
        AdsHelperClass.setapp_updateAppDialogStatus(appData.getApp_updateAppDialogStatus());
        AdsHelperClass.setapp_versionCode(appData.getApp_versionCode());
        AdsHelperClass.setapp_email_id(appData.getApp_email_id());
        AdsHelperClass.setapp_policy_url(appData.getApp_policy_url());
        AdsHelperClass.setIn_appreview(appData.getIn_appreview());
        AdsHelperClass.setFirst_ad_hide(appData.getFirst_ad_hide());

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

    }

    private static void initAd(List<String> testDeviceIds) {

        if (AdsHelperClass.getAdShowStatus() == 0) {
            return;
        }

        if (AdsHelperClass.getAdmobAdStatus() == 1 || AdsHelperClass.getAdXAdStatus() == 1) {
            MobileAds.initialize(activity, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                    RequestConfiguration configuration =
                            new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
                    MobileAds.setRequestConfiguration(configuration);
                    PrintLog(TAG, "onInitializationComplete: admob");

                }
            });


        }

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
        if (AdsHelperClass.getAdShowStatus() == 1) {
            AppManage.getInstance(activity).PreLoadNative();
        }


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
                listner.onSuccess();
                initAd(testDeviceIds);
            }
        }
    }


    private void loadNextInterstitialPlatform() {
        hideDialog();

        if (interstitial_sequence.size() != 0) {
            interstitial_sequence.remove(0);

            if (interstitial_sequence.size() != 0 && interstitial_sequence.get(0) != null) {
                PrintLog(TAG, "loadNextInterstitialPlatform: " + interstitial_sequence);
                displayInterstitialAds(interstitial_sequence.get(0), activity);
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


    public void showInterstitialAd(Context context, MyInterstitialCallback myCallback) {
        turnInterstitialAd(context, myCallback, 0);
    }

    public void showInterstitialAd(Context context, MyInterstitialCallback myCallback, int how_many_clicks) {
        turnInterstitialAd(context, myCallback, how_many_clicks);

    }

    public void showInterstitialAdSplash(Context context, MyInterstitialCallback myCallback) {
        turnInterstitialAdSplash(context, myCallback);

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


        String adPlatformSequence = AdsHelperClass.getInterSequence();


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

                      /*  AdsHelperClass.isShowingFullScreenAd = false;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                        interstitialCallBack();*/
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


                   /*     AdsHelperClass.isShowingFullScreenAd = false;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                        interstitialCallBack();*/
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

                        interstitialCallBack();
                        AdsHelperClass.isShowingFullScreenAd = false;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = false;

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {
                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
                        PrintLog(TAG, "Applovin onAdLoadFailed ===> onAdLoadFailed" + error.getMessage());

                        appLovin_interstitialAd = null;
                        loadNextInterstitialPlatformSplash();

                 /*       AdsHelperClass.isShowingFullScreenAd = false;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                        interstitialCallBack();  */

                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                        PrintLog(TAG, "Applovin onAdDisplayFailed ===> onAdDisplayFailed" + error.getMessage());

                        appLovin_interstitialAd = null;

                       /* AdsHelperClass.isShowingFullScreenAd = false;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                        interstitialCallBack();*/
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
                    /*    interstitialCallBack();
                        AdsHelperClass.isShowingFullScreenAd = false;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = false;
*/
                        loadNextInterstitialPlatformSplash();


                    }

                    @Override
                    public void onInterstitialAdOpened() {
                        AdsHelperClass.isShowingFullScreenAd = true;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = true;

                    }

                    @Override
                    public void onInterstitialAdClosed() {
                        interstitialCallBack();
                        AdsHelperClass.isShowingFullScreenAd = false;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = false;

                    }

                    @Override
                    public void onInterstitialAdShowSucceeded() {
                    }

                    @Override
                    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
                /*        interstitialCallBack();
                        AdsHelperClass.isShowingFullScreenAd = false;
                        AdsHelperClass.isIsShowingFullScreenAdSplash = false;*/
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

    private void displayInterstitialAd(String platform, final Context context) {

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

              /*          AdsHelperClass.isShowingFullScreenAd = false;
                        interstitialCallBack();*/
                        loadNextInterstitialPlatform();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
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

                        loadNextInterstitialPlatform();

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

                        interstitialCallBack();
                        AdsHelperClass.isShowingFullScreenAd = false;

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {
                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
                        PrintLog(TAG, "onAdLoadFailed: " + error.getMessage());

                        appLovin_interstitialAd = null;
                        loadNextInterstitialPlatform();


                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                        PrintLog(TAG, "onAdDisplayFailed: " + error.getMessage());

                        appLovin_interstitialAd = null;
                        loadNextInterstitialPlatform();


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

                        loadNextInterstitialPlatform();


                    }

                    @Override
                    public void onInterstitialAdOpened() {
                        AdsHelperClass.isShowingFullScreenAd = true;

                    }

                    @Override
                    public void onInterstitialAdClosed() {

                        loadNextInterstitialPlatform();

                    }

                    @Override
                    public void onInterstitialAdShowSucceeded() {
                    }

                    @Override
                    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
                        PrintLog(TAG, "onInterstitialAdShowFailed: " + ironSourceError.getErrorMessage());


                        loadNextInterstitialPlatform();


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

            intermyCallback.callbackCall();
            intermyCallback = null;
            return;
        }


        if (how_many_clicks != 0) {
            if (count_click % how_many_clicks != 0) {
                if (intermyCallback != null) {
                    intermyCallback.callbackCall();
                    intermyCallback = null;
                }
                return;
            }
        }

        String adPlatformSequence = AdsHelperClass.getInterSequence();


        interstitial_sequence = new ArrayList<String>();
        if (/*app_howShowAd == 0 && */!adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");

            interstitial_sequence.addAll(Arrays.asList(adSequence));

        }


        if (interstitial_sequence.size() != 0) {
            displayInterstitialAds(interstitial_sequence.get(0), context);
        } else {
            interstitialCallBack();
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

    private void displayInterstitialAds(String platform, final Context activity) {

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
                    mInterstitialAd = interstitialAd;
                    PrintLog(TAG, "onAdLoaded");
                    hideDialog();
                    displayInterstitialAd(AdsHelperClass.ADMOB, activity);


                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    PrintLog(TAG, loadAdError.getMessage());
                    mInterstitialAd = null;
                    loadNextInterstitialPlatform();

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
                    loadNextInterstitialPlatform();


                }

                @Override
                public void onAdLoaded(Ad ad) {
                    mFbInterstitialAd = (com.facebook.ads.InterstitialAd) ad;
                    PrintLog(TAG, "onAdLoaded");
                    hideDialog();
                    displayInterstitialAd(AdsHelperClass.FACEBOOK, activity);

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
                            mAdManagerInterstitialAd = interstitialAd;
                            hideDialog();
                            displayInterstitialAd(AdsHelperClass.ADX, activity);


                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            PrintLog(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());

                            mAdManagerInterstitialAd = null;
                            loadNextInterstitialPlatform();

                        }
                    });


        } else if (platform.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getapplovininter())) {
                return;
            }
            showDialog(activity);

            appLovin_interstitialAd = new MaxInterstitialAd(AdsHelperClass.getapplovininter(), (Activity) activity);
            appLovin_interstitialAd.loadAd();

            appLovin_interstitialAd.setListener(new MaxAdListener() {
                @Override
                public void onAdLoaded(MaxAd ad) {
                    PrintLog(TAG, "Applovin InterstitialAd ===> onAdLoaded");
                    hideDialog();

                    displayInterstitialAd(AdsHelperClass.APPLOVIN, activity);
                }

                @Override
                public void onAdDisplayed(MaxAd ad) {
                    appLovin_interstitialAd = null;
                    AdsHelperClass.isShowingFullScreenAd = true;

                }

                @Override
                public void onAdHidden(MaxAd ad) {
                    interstitialCallBack();
                    AdsHelperClass.isShowingFullScreenAd = false;

                }

                @Override
                public void onAdClicked(MaxAd ad) {
                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    PrintLog(TAG, "onAdLoadFailed: " + error.getMessage());

                    appLovin_interstitialAd = null;
                    loadNextInterstitialPlatform();


                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    PrintLog(TAG, "onAdDisplayFailed: " + error.getMessage());

                    appLovin_interstitialAd = null;

                    loadNextInterstitialPlatform();


                }
            });


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

                    displayInterstitialAd(AdsHelperClass.IRON, activity);
                }

                @Override
                public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                    PrintLog(TAG, ironSourceError.getErrorMessage());

                    loadNextInterstitialPlatform();


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


                    loadNextInterstitialPlatform();


                }

                @Override
                public void onInterstitialAdClicked() {

                }
            });


        } else {
            interstitialCallBack();
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
                            PrintLog(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());

                            mAdManagerInterstitialAd = null;

                            loadNextInterstitialPlatformSplash();

                        }
                    });


        } else if (platform.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {
            if (TextUtils.isEmpty(AdsHelperClass.getapplovininter())) {
                return;
            }
            appLovin_interstitialAd = new MaxInterstitialAd(AdsHelperClass.getapplovininter(), (Activity) activity);
            appLovin_interstitialAd.loadAd();

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
                    interstitialCallBack();
                    AdsHelperClass.isShowingFullScreenAd = false;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = false;

                }

                @Override
                public void onAdClicked(MaxAd ad) {
                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    PrintLog(TAG, "Applovin onAdLoadFailed ===> onAdLoadFailed" + error.getMessage());

                    appLovin_interstitialAd = null;

                /*    AdsHelperClass.isShowingFullScreenAd = false;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                    interstitialCallBack();*/
                    loadNextInterstitialPlatformSplash();

                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    PrintLog(TAG, "Applovin onAdDisplayFailed ===> onAdDisplayFailed" + error.getMessage());

                    appLovin_interstitialAd = null;

                   /* AdsHelperClass.isShowingFullScreenAd = false;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                    interstitialCallBack();*/
                    loadNextInterstitialPlatformSplash();


                }
            });


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

               /*     AdsHelperClass.isShowingFullScreenAd = false;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = false;
                    interstitialCallBack();*/
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

                   /* interstitialCallBack();
                    AdsHelperClass.isShowingFullScreenAd = false;
                    AdsHelperClass.isIsShowingFullScreenAdSplash = false;*/
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


//        SharedPreferences.Editor nativeEditor = mysharedpreferences.edit();
//        this.admob_b = ADMOB_B1;
//        nativeEditor.putInt("AdsBannerSwipeCount", 2);
//        nativeEditor.commit();

//        count_banner++;


//        int app_howShowAd = mysharedpreferences.getInt("app_howShowAdBanner", 0);
//        String adPlatformSequence = mysharedpreferences.getString("app_adPlatformSequenceBanner", "");
//        String alernateAdShow = mysharedpreferences.getString("app_alernateAdShowBanner", "");

        String adPlatformSequence = AdsHelperClass.getbannerSequence();

        banner_sequence = new ArrayList<String>();
        if (/*app_howShowAd == 0 && */!adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            banner_sequence.addAll(Arrays.asList(adSequence));

        }/* else if (app_howShowAd == 1 && !alernateAdShow.isEmpty()) {
            String alernateAd[] = alernateAdShow.split(",");

            int index = 0;
            for (int j = 0; j <= 10; j++) {
                if (count_banner % alernateAd.length == j) {
                    index = j;
                    banner_sequence.add(alernateAd[index]);
                }
            }

            String adSequence[] = adPlatformSequence.split(",");
            for (int j = 0; j < adSequence.length; j++) {
                if (banner_sequence.size() != 0) {
                    if (!banner_sequence.get(0).equals(adSequence[j])) {
                        banner_sequence.add(adSequence[j]);
                    }
                }
            }
        }*/

        if (banner_sequence.size() != 0) {
            displayBanner(banner_sequence.get(0), banner_container);
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
            showAdmobBanner(banner_container);
        } else if (platform.equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {
            showAdxBanner(banner_container);
        } else if (platform.equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {
            showFBBanner(banner_container);
        } else if (platform.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {
            showAppLovinBanner(banner_container);
        } else if (platform.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
            showIronSourceBanner(banner_container);
        }
    }

    private void showAdxBanner(ViewGroup banner_container) {
        if (AdsHelperClass.getAdxBannerId().isEmpty()) {
            return;
        }

        AdManagerAdView adXManagerAdView = new AdManagerAdView(activity);
        adXManagerAdView.setAdUnitId(AdsHelperClass.getAdxBannerId());
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize(activity);
        adXManagerAdView.setAdSize(adSize);
        adXManagerAdView.loadAd(adRequest);

//        final AdView mAdView = new AdView(activity);
//        mAdView.setAdSize(AdSize.BANNER);
//        mAdView.setAdUnitId(AdsHelperClass.getAdxBannerId());
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
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
                banner_container.setVisibility(View.GONE);

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
                PrintLog(TAG, "onAdImpression: AdmobBanner");

            }
        });


    }

    private void showAdmobBanner(final ViewGroup banner_container) {
        if (AdsHelperClass.getAdmobBannerId().isEmpty()) {
            return;
        }

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
                banner_container.setVisibility(View.GONE);
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

    private void showFBBanner(final ViewGroup banner_container) {
        if (AdsHelperClass.getFBBannerId().isEmpty()) {
            return;
        }

        final com.facebook.ads.AdView mAdView = new com.facebook.ads.AdView(activity, AdsHelperClass.getFBBannerId(), com.facebook.ads.AdSize.BANNER_HEIGHT_50);

        mAdView.loadAd(mAdView.buildLoadAdConfig().withAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                PrintLog(TAG, "onError:FBBanner " + adError.getErrorMessage());

                banner_container.removeAllViews();
                banner_container.setVisibility(View.GONE);
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

    private void showAppLovinBanner(final ViewGroup banner_container) {
        if (AdsHelperClass.getapplovinbanner().isEmpty()) {
            return;
        }

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
                banner_container.setVisibility(View.GONE);
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

    private void showIronSourceBanner(ViewGroup banner_container) {
        if (AdsHelperClass.getironappkey().isEmpty()) {
            return;
        }

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
                banner_container.setVisibility(View.GONE);
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

    public void ResumeIronSourceBanner(ViewGroup banner_container, Activity activity) {
        if (AdsHelperClass.getbannerSequence().equalsIgnoreCase(AdsHelperClass.IRON)) {
            if (mIronSourceBannerLayout != null) {
                IronSource.destroyBanner(mIronSourceBannerLayout);
                showIronSourceBanner(banner_container);
                IronSource.onResume(activity);
            }
        }

    }


    public void PreLoadNative() {

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
            LoadPreloadNative(native_sequence.get(0));
        }


    }

    private void LoadPreloadNative(String s) {
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
                                nextPreNativePlatform();

                            }
                        })
                        .withNativeAdOptions(new NativeAdOptions.Builder()
                                // Methods in the NativeAdOptions.Builder class can be
                                // used here to specify individual options settings.
                                .build())
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
                final AdLoader adLoader = new AdLoader.Builder(activity, AdsHelperClass.getAdxNativeId())
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
                                nextPreNativePlatform();
                            }
                        })
                        .withNativeAdOptions(new NativeAdOptions.Builder()
                                // Methods in the NativeAdOptions.Builder class can be
                                // used here to specify individual options settings.
                                .build())
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
                        nextPreNativePlatform();

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
                        nextPreNativePlatform();
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

                                nextPreNativePlatform();
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

    public void PreLoadShowNative(ViewGroup nativeAdContainer, boolean b) {


        if (native_sequence.size() != 0) {
            if (preAdmobNative != null && native_sequence.get(0).equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {
                PrintLog(TAG, "PreLoadNative: ShowLoaded");
                new Inflate_ADS(activity).inflate_NATIV_ADMOB(preAdmobNative, nativeAdContainer, b);
            } else if (preAdxNative != null && native_sequence.get(0).equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {
                PrintLog(TAG, "PreLoadNative: ShowLoaded");
                new Inflate_ADS(activity).inflate_NATIV_ADMOB(preAdxNative, nativeAdContainer, b);
            } else if (preFbNative != null && native_sequence.get(0).equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {
                PrintLog(TAG, "PreLoadNative: ShowLoaded");
                new Inflate_ADS(activity).inflate_NATIV_FB(preFbNative, nativeAdContainer, b);

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
                if (b) {
                    final float scale = activity.getResources().getDisplayMetrics().density;
                    int pixels = (int) (250 * scale + 0.5f);

                    nativeAdContainer.getLayoutParams().height = pixels;
                    nativeAdContainer.requestLayout();
                }
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
                PreLoadNative();
            }
        }

    }


    private void nextPreNativePlatform() {
        if (native_sequence.size() != 0) {
            native_sequence.remove(0);
            if (native_sequence.size() != 0) {
                LoadPreloadNative(native_sequence.get(0));
            }
        }
    }

    private void nextNativePlatform(ViewGroup nativeAdContainer, boolean b) {
        if (native_sequence.size() != 0) {
            native_sequence.remove(0);
            if (native_sequence.size() != 0) {
                displayNative(native_sequence.get(0), nativeAdContainer, b);
            }
        }
    }

    public void showNative(ViewGroup nativeAdContainer, boolean b) {

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
        if (/*app_howShowAd == 0 &&*/ !adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            for (int i = 0; i < adSequence.length; i++) {
                native_sequence.add(adSequence[i]);
            }

        } /*else if (app_howShowAd == 1 && !alernateAdShow.isEmpty()) {
            String alernateAd[] = alernateAdShow.split(",");

            int index = 0;
            for (int j = 0; j <= 10; j++) {
                if (count_native % alernateAd.length == j) {
                    index = j;
                    native_sequence.add(alernateAd[index]);
                }
            }

            String adSequence[] = adPlatformSequence.split(",");
            for (int j = 0; j < adSequence.length; j++) {
                if (native_sequence.size() != 0) {
                    if (!native_sequence.get(0).equals(adSequence[j])) {
                        native_sequence.add(adSequence[j]);
                    }
                }
            }
        }*/

        if (native_sequence.size() != 0) {
            displayNative(native_sequence.get(0), nativeAdContainer, b);
        }

    }

    public void displayNative(String platform, ViewGroup nativeAdContainer, boolean b) {

        if (platform.equals(AdsHelperClass.ADMOB) && AdsHelperClass.getAdmobAdStatus() == 1) {
            showAdmobNative(nativeAdContainer, b);
        } else if (platform.equals(AdsHelperClass.ADX) && AdsHelperClass.getAdXAdStatus() == 1) {
            showAdxNative(nativeAdContainer, b);
        } else if (platform.equals(AdsHelperClass.FACEBOOK) && AdsHelperClass.getFBAdStatus() == 1) {
            showFBNative(nativeAdContainer, b);
        } else if (platform.equals(AdsHelperClass.APPLOVIN) && AdsHelperClass.getApplovinAdStatus() == 1) {
            showAppLovinNativeCustom(nativeAdContainer, b);

        } else if (platform.equals(AdsHelperClass.IRON) && AdsHelperClass.getIronSourceAdStatus() == 1) {
            showIronNative(nativeAdContainer, b);

        }
    }

    private void showAdmobNative(final ViewGroup nativeAdContainer, boolean b) {
        if (AdsHelperClass.getAdmobNativeId().isEmpty()) {
            nextNativePlatform(nativeAdContainer, b);
            return;
        }

        final AdLoader adLoader = new AdLoader.Builder(activity, AdsHelperClass.getAdmobNativeId())
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        // Show the ad.
                        new Inflate_ADS(activity).inflate_NATIV_ADMOB(nativeAd, nativeAdContainer, b);


                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        PrintLog(TAG, "onAdFailedToLoad:native " + adError.getMessage());

                        // Handle the failure by logging, altering the UI, and so on.
                        nativeAdContainer.removeAllViews();
                        nativeAdContainer.setVisibility(View.GONE);
                        nextNativePlatform(nativeAdContainer, b);

                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }

    private void showFBNative(final ViewGroup nativeAdContainer, boolean b) {
        if (AdsHelperClass.getFBNativeId().isEmpty()) {
            return;
        }

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

                        nativeAdContainer.removeAllViews();
                        nativeAdContainer.setVisibility(View.GONE);
                        nextNativePlatform(nativeAdContainer, b);

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        PrintLog(TAG, "onAdLoaded: ");

                        new Inflate_ADS(activity).inflate_NATIV_FB(nativeAd1, nativeAdContainer, b);

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

    private void showAdxNative(final ViewGroup nativeAdContainer, boolean b) {
        if (AdsHelperClass.getAdxNativeId().isEmpty()) {
            return;
        }

        final AdLoader adLoader = new AdLoader.Builder(activity, AdsHelperClass.getAdxNativeId())
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        // Show the ad.
                        PrintLog(TAG, "onNativeAdLoaded: Adx");


                        new Inflate_ADS(activity).inflate_NATIV_ADMOB(nativeAd, nativeAdContainer, b);


                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        PrintLog(TAG, "onAdFailedToLoad: Adx" + adError.getMessage());

                        nativeAdContainer.removeAllViews();
                        nativeAdContainer.setVisibility(View.GONE);
                        nextNativePlatform(nativeAdContainer, b);

                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }


    private void showAppLovinNativeCustom(final ViewGroup nativeAdContainer, boolean b) {
        if (AdsHelperClass.getapplovinnative().isEmpty()) {
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
                if (b) {
                    final float scale = activity.getResources().getDisplayMetrics().density;
                    int pixels = (int) (250 * scale + 0.5f);

                    nativeAdContainer.getLayoutParams().height = pixels;
                    nativeAdContainer.requestLayout();

                }
                nativeAdContainer.addView(nativeAdView);
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {

                PrintLog(TAG, "onNativeAdLoadFailed: ");

                nativeAdContainer.removeAllViews();
                nativeAdContainer.setVisibility(View.GONE);
                // We recommend retrying with exponentially higher delays up to a maximum delay
                nextNativePlatform(nativeAdContainer, b);

            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {
                // Optional click callback
            }
        });

    }

    private void showIronNative(ViewGroup nativeAdContainer, boolean b) {
        if (AdsHelperClass.getironappkey().isEmpty()) {
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
                nextNativePlatform(nativeAdContainer, b);

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

    public void ResumeIronSourceNativePre(FrameLayout banner_container, Activity activity, boolean b) {
        if (AdsHelperClass.getbannerSequence().equalsIgnoreCase(AdsHelperClass.IRON)) {
            if (preIronSourceNativeLayout != null) {
                IronSource.destroyBanner(preIronSourceNativeLayout);
                showIronNative(banner_container, b);
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

    public void ResumeIronSourceNative(FrameLayout banner_container, Activity activity, boolean b) {
        if (AdsHelperClass.getbannerSequence().equalsIgnoreCase(AdsHelperClass.IRON)) {
            if (mIronSourceNativeLayout != null) {
                IronSource.destroyBanner(mIronSourceNativeLayout);
                showIronNative(banner_container, b);
                IronSource.onResume(activity);
            }
        }

    }

    public interface MyInterstitialCallback {
        void callbackCall();

    }


}
