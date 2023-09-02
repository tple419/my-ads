package com.module.adsdk;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdsHelperClass {
    public static final String ADMOB = "admob";
    public static final String ADX = "adx";
    public static final String FACEBOOK = "fb";
    public static final String IRON = "iron";
    public static final String APPLOVIN = "applovin";
    public static boolean isShowingFullScreenAd = false;
    public static boolean isIsShowingFullScreenAdSplash = false;
    public static String instanceCount = "instance_count";

    public static void showUpdateDialog(Activity activity, final String s) {

        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        View view = activity.getLayoutInflater().inflate(R.layout.ads_dialog_install_new_app, null);
        dialog.setContentView(view);
        TextView update = view.findViewById(R.id.update);
        TextView txt_title = view.findViewById(R.id.txt_title);
        TextView txt_decription = view.findViewById(R.id.txt_decription);
        update.setText(activity.getString(R.string.ads_txt_update_now));
        txt_title.setText(activity.getString(R.string.ads_txt_update_our_new_app_now_and_enjoy));
        txt_decription.setText("");
        txt_decription.setVisibility(View.GONE);

        update.setOnClickListener(view1 -> {
            try {
                Uri marketUri = Uri.parse(s);
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                activity.startActivity(marketIntent);
            } catch (ActivityNotFoundException e) {
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public static void showRedirectDialog(Activity activity, final String s) {

        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        View view = activity.getLayoutInflater().inflate(R.layout.ads_dialog_install_new_app, null);
        dialog.setContentView(view);
        TextView update = view.findViewById(R.id.update);
        TextView txt_title = view.findViewById(R.id.txt_title);
        TextView txt_decription = view.findViewById(R.id.txt_decription);

        update.setText(activity.getString(R.string.ads_txt_install_now));
        txt_title.setText(activity.getString(R.string.ads_txt_install_our_new_app_now_and_enjoy));
        txt_decription.setText(activity.getString(R.string.ads_txt_install_our_new_app_now_and_enjoy_decription));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri marketUri = Uri.parse(s);
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    activity.startActivity(marketIntent);
                } catch (ActivityNotFoundException ignored1) {
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public static String getRemoteData() {
        return SharedPreferencesClass.getInstance().getString("REMOTE_CONFIG_DATA", "");
    }

    public static void setRemoteData(String i) {
        SharedPreferencesClass.getInstance().setString("REMOTE_CONFIG_DATA", i);
    }

    public static int getAdShowStatus() {
        return SharedPreferencesClass.getInstance().getInt("AdShowStatus", 0);
    }

    public static void setAdShowStatus(int i) {
        SharedPreferencesClass.getInstance().setInt("AdShowStatus", i);
    }

    public static int getOpenAdsShowedCount() {
        return SharedPreferencesClass.getInstance().getInt("OpenAdShowCount", 0);
    }

    public static void setOpenAdsShowedCount(int i) {
        SharedPreferencesClass.getInstance().setInt("OpenAdShowCount", i);
    }

    public static int getFullScreenAdsShowedCount() {
        return SharedPreferencesClass.getInstance().getInt("FullScreenAdShowCount", 0);
    }

    public static void setFullScreenAdsShowedCount(int i) {
        SharedPreferencesClass.getInstance().setInt("FullScreenAdShowCount", i);
    }

    public static int getAdmobAdStatus() {
        return SharedPreferencesClass.getInstance().getInt("AdmobAdStatus", 0);
    }

    public static void setAdmobAdStatus(int i) {
        SharedPreferencesClass.getInstance().setInt("AdmobAdStatus", i);
    }

    public static int getFBAdStatus() {
        return SharedPreferencesClass.getInstance().getInt("AdmobFBStatus", 0);
    }

    public static void setFBAdStatus(int i) {
        SharedPreferencesClass.getInstance().setInt("AdmobFBStatus", i);
    }

    public static int getAdXAdStatus() {
        return SharedPreferencesClass.getInstance().getInt("AdXAdStatus", 0);
    }

    public static void setAdXAdStatus(int i) {
        SharedPreferencesClass.getInstance().setInt("AdXAdStatus", i);
    }

    public static int getApplovinAdStatus() {
        return SharedPreferencesClass.getInstance().getInt("ApplovinAdStatus", 0);
    }

    public static void setApplovinAdStatus(int i) {
        SharedPreferencesClass.getInstance().setInt("ApplovinAdStatus", i);
    }

    public static int getIronSourceAdStatus() {
        return SharedPreferencesClass.getInstance().getInt("IronSourceAdStatus", 0);
    }

    public static void setIronSourceAdStatus(int i) {
        SharedPreferencesClass.getInstance().setInt("IronSourceAdStatus", i);
    }

    public static int getbannerAdStatus() {
        return SharedPreferencesClass.getInstance().getInt("bannerAdStatus", 0);
    }

    public static void setbannerAdStatus(int i) {
        SharedPreferencesClass.getInstance().setInt("bannerAdStatus", i);
    }

    public static int getnativeAdStatus() {
        return SharedPreferencesClass.getInstance().getInt("nativeAdStatus", 0);
    }

    public static void setnativeAdStatus(int i) {
        SharedPreferencesClass.getInstance().setInt("nativeAdStatus", i);
    }

    public static int getinterAdStatus() {
        return SharedPreferencesClass.getInstance().getInt("interAdStatus", 0);
    }

    public static void setinterAdStatus(int i) {
        SharedPreferencesClass.getInstance().setInt("interAdStatus", i);
    }

    public static int getappOpenAdStatus() {
        return SharedPreferencesClass.getInstance().getInt("appOpenAdStatus", 0);
    }

    public static void setappOpenAdStatus(int i) {
        SharedPreferencesClass.getInstance().setInt("appOpenAdStatus", i);
    }

    public static int getappOpenCount() {
        return SharedPreferencesClass.getInstance().getInt("appOpenCount", 0);
    }

    public static void setappOpenCount(int i) {
        SharedPreferencesClass.getInstance().setInt("appOpenCount", i);
    }

    public static int getFullScreenLimitCount() {
        return SharedPreferencesClass.getInstance().getInt("setFullScreenLimitCount", 0);
    }

    public static void setFullScreenLimitCount(int i) {
        SharedPreferencesClass.getInstance().setInt("setFullScreenLimitCount", i);
    }

    public static int getinterstitialCount() {
        return SharedPreferencesClass.getInstance().getInt("interstitialCount", 0);
    }

    public static void setinterstitialCount(int i) {
        SharedPreferencesClass.getInstance().setInt("interstitialCount", i);
    }
    public static int getInterstitialCountMultiplier() {
        return SharedPreferencesClass.getInstance().getInt("interstitialCountMultiplier", 1);
    }

    public static void setInterstitialCountMultiplier(int i) {
        SharedPreferencesClass.getInstance().setInt("interstitialCountMultiplier", i);
    }

    public static int getExitAdEnable() {
        return SharedPreferencesClass.getInstance().getInt("exitAdEnable", 0);
    }

    public static void setExitAdEnable(int i) {
        SharedPreferencesClass.getInstance().setInt("exitAdEnable", i);
    }

    public static int getSplash_ad_type() {
        return SharedPreferencesClass.getInstance().getInt("splash_ad_type", 0);
    }

    public static void setSplash_ad_type(int i) {
        SharedPreferencesClass.getInstance().setInt("splash_ad_type", i);
    }

    public static int getIs_splash_on() {
        return SharedPreferencesClass.getInstance().getInt("is_splash_on", 0);
    }

    public static void setIs_splash_on(int i) {
        SharedPreferencesClass.getInstance().setInt("is_splash_on", i);
    }

    public static int getSplash_time() {
        return SharedPreferencesClass.getInstance().getInt("splash_time", 0);
    }

    public static void setSplash_time(int i) {
        SharedPreferencesClass.getInstance().setInt("splash_time", i);
    }

    public static String getAdmobBannerId() {
        return SharedPreferencesClass.getInstance().getString("AdmobBannerId", "");
    }

    public static void setAdmobBannerId(String i) {
        SharedPreferencesClass.getInstance().setString("AdmobBannerId", i);
    }

    public static String getAdmobNativeId() {
        return SharedPreferencesClass.getInstance().getString("AdmobNativeId", "");
    }
public static String getAdmobSmallNativeId() {
        return SharedPreferencesClass.getInstance().getString("AdmobSmallNativeId", "");
    }

    public static void setAdmobNativeId(String i) {
        SharedPreferencesClass.getInstance().setString("AdmobNativeId", i);
    }
public static void setAdmobSmallNativeId(String i) {
        SharedPreferencesClass.getInstance().setString("AdmobSmallNativeId", i);
    }

    public static String getAdmobInterId() {
        return SharedPreferencesClass.getInstance().getString("AdmobInterId", "");
    }

    public static void setAdmobInterId(String i) {
        SharedPreferencesClass.getInstance().setString("AdmobInterId", i);
    }

    public static String getAdmobAppOpenId() {
        return SharedPreferencesClass.getInstance().getString("AdmobAppOpenId", "");
    }

    public static void setAdmobAppOpenId(String i) {
        SharedPreferencesClass.getInstance().setString("AdmobAppOpenId", i);
    }

    public static String getAdxBannerId() {
        return SharedPreferencesClass.getInstance().getString("AdxBannerId", "");
    }

    public static void setAdxBannerId(String i) {
        SharedPreferencesClass.getInstance().setString("AdxBannerId", i);
    }

    public static String getAdxNativeId() {
        return SharedPreferencesClass.getInstance().getString("AdxNativeId", "");
    }

    public static void setAdxNativeId(String i) {
        SharedPreferencesClass.getInstance().setString("AdxNativeId", i);
    }

    public static String getAdxSmallNativeId() {
        return SharedPreferencesClass.getInstance().getString("AdxSmallNativeId", "");
    }

    public static void setAdxSmallNativeId(String i) {
        SharedPreferencesClass.getInstance().setString("AdxSmallNativeId", i);
    }

    public static String getAdxInterId() {
        return SharedPreferencesClass.getInstance().getString("AdxInterId", "");
    }

    public static void setAdxInterId(String i) {
        SharedPreferencesClass.getInstance().setString("AdxInterId", i);
    }

    public static String getFBBannerId() {
        return SharedPreferencesClass.getInstance().getString("FBBannerId", "");
    }

    public static void setFBBannerId(String i) {
        SharedPreferencesClass.getInstance().setString("FBBannerId", i);
    }

    public static String getFBNativeId() {
        return SharedPreferencesClass.getInstance().getString("FBNativeId", "");
    }

    public static void setFBNativeId(String i) {
        SharedPreferencesClass.getInstance().setString("FBNativeId", i);
    }
public static String getFBSmallNativeId() {
        return SharedPreferencesClass.getInstance().getString("FBSmallNative", "");
    }

    public static void setFBSmallNativeId(String i) {
        SharedPreferencesClass.getInstance().setString("FBSmallNative", i);
    }

    public static String getFBInterId() {
        return SharedPreferencesClass.getInstance().getString("FBInterId", "");
    }

    public static void setFBInterId(String i) {
        SharedPreferencesClass.getInstance().setString("FBInterId", i);
    }

    public static String getAdxAppOpenId() {
        return SharedPreferencesClass.getInstance().getString("AdxAppOpenId", "");
    }

    public static void setAdxAppOpenId(String i) {
        SharedPreferencesClass.getInstance().setString("AdxAppOpenId", i);
    }

    public static String getapplovinbanner() {
        return SharedPreferencesClass.getInstance().getString("applovinbanner", "");
    }

    public static void setapplovinbanner(String i) {
        SharedPreferencesClass.getInstance().setString("applovinbanner", i);
    }

    public static String getapplovinnative() {
        return SharedPreferencesClass.getInstance().getString("applovinnative", "");
    }

    public static void setapplovinnative(String i) {
        SharedPreferencesClass.getInstance().setString("applovinnative", i);
    }

    public static String getapplovininter() {
        return SharedPreferencesClass.getInstance().getString("applovininter", "");
    }

    public static void setapplovininter(String i) {
        SharedPreferencesClass.getInstance().setString("applovininter", i);
    }

    public static String getapplovinOpenAdId() {
        return SharedPreferencesClass.getInstance().getString("applovinOpenAdId", "");
    }

    public static void setapplovinOpenAdId(String i) {
        SharedPreferencesClass.getInstance().setString("applovinOpenAdId", i);
    }

    public static String getironappkey() {
        return SharedPreferencesClass.getInstance().getString("ironappkey", "");
    }

    public static void setironappkey(String i) {
        SharedPreferencesClass.getInstance().setString("ironappkey", i);
    }

    public static String getbannerSequence() {
        return SharedPreferencesClass.getInstance().getString("bannerSequence", "");
    }

    public static void setbannerSequence(String i) {
        SharedPreferencesClass.getInstance().setString("bannerSequence", i);
    }

    public static String getnativeSequence() {
        return SharedPreferencesClass.getInstance().getString("nativeSequence", "");
    }

    public static void setnativeSequence(String i) {
        SharedPreferencesClass.getInstance().setString("nativeSequence", i);
    }

    public static String getInterSequence() {
        return SharedPreferencesClass.getInstance().getString("InterSequence", "");
    }

    public static void setInterSequence(String i) {
        SharedPreferencesClass.getInstance().setString("InterSequence", i);
    }

    public static String getSplashAdsSequence() {
        return SharedPreferencesClass.getInstance().getString("SplashAdsSequence", "");
    }

    public static void setSplashAdsSequence(String i) {
        SharedPreferencesClass.getInstance().setString("SplashAdsSequence", i);
    }

    public static String getAppOpenSequence() {
        return SharedPreferencesClass.getInstance().getString("AppOpenSequence", "");
    }

    public static void setAppOpenSequence(String i) {
        SharedPreferencesClass.getInstance().setString("AppOpenSequence", i);
    }

    public static String getapp_versionCode() {
        return SharedPreferencesClass.getInstance().getString("app_versionCode", "");
    }

    public static void setapp_versionCode(String i) {
        SharedPreferencesClass.getInstance().setString("app_versionCode", i);
    }

    public static String getapp_policy_url() {
        return SharedPreferencesClass.getInstance().getString("app_policy_url", "");
    }

    public static void setapp_policy_url(String i) {
        SharedPreferencesClass.getInstance().setString("app_policy_url", i);
    }

    public static String getapp_email_id() {
        return SharedPreferencesClass.getInstance().getString("app_email_id", "");
    }

    public static void setapp_email_id(String i) {
        SharedPreferencesClass.getInstance().setString("app_email_id", i);
    }

    public static int getapp_updateAppDialogStatus() {
        return SharedPreferencesClass.getInstance().getInt("app_updateAppDialogStatus", 0);
    }

    public static void setapp_updateAppDialogStatus(int i) {
        SharedPreferencesClass.getInstance().setInt("app_updateAppDialogStatus", i);
    }

    public static String getapp_newPackageName() {
        return SharedPreferencesClass.getInstance().getString("app_newPackageName", "");
    }

    public static void setapp_newPackageName(String i) {
        SharedPreferencesClass.getInstance().setString("app_newPackageName", i);
    }

    public static int getapp_redirectOtherAppStatus() {
        return SharedPreferencesClass.getInstance().getInt("app_redirectOtherAppStatus", 0);
    }

    public static void setapp_redirectOtherAppStatus(int i) {
        SharedPreferencesClass.getInstance().setInt("app_redirectOtherAppStatus", i);
    }

    public static int getNative_display_list() {
        return SharedPreferencesClass.getInstance().getInt("native_display_list", 0);
    }

    public static void setNative_display_list(int i) {
        SharedPreferencesClass.getInstance().setInt("native_display_list", i);
    }

    public static int getBanner_display_pager() {
        return SharedPreferencesClass.getInstance().getInt("banner_display_pager", 0);
    }

    public static void setBanner_display_pager(int i) {
        SharedPreferencesClass.getInstance().setInt("banner_display_pager", i);
    }

    public static int getBanner_display_list() {
        return SharedPreferencesClass.getInstance().getInt("banner_display_list", 0);
    }

    public static void setBanner_display_list(int i) {
        SharedPreferencesClass.getInstance().setInt("banner_display_list", i);
    }

    public static int getReview_popup_count() {
        return SharedPreferencesClass.getInstance().getInt("review_popup_count", 0);
    }

    public static void setReview_popup_count(int i) {
        SharedPreferencesClass.getInstance().setInt("review_popup_count", i);
    }

    public static int getDirect_review_enable() {
        return SharedPreferencesClass.getInstance().getInt("direct_review_enable", 0);
    }

    public static void setDirect_review_enable(int i) {
        SharedPreferencesClass.getInstance().setInt("direct_review_enable", i);
    }

    public static int getAlbum_click_enabled() {
        return SharedPreferencesClass.getInstance().getInt("album_click_enabled", 0);
    }

    public static void setAlbum_click_enabled(int i) {
        SharedPreferencesClass.getInstance().setInt("album_click_enabled", i);
    }

    public static int getIn_appreview() {
        return SharedPreferencesClass.getInstance().getInt("in_appreview", 0);
    }

    public static void setIn_appreview(int i) {
        SharedPreferencesClass.getInstance().setInt("in_appreview", i);
    }

    public static int getFirst_ad_hide() {
        return SharedPreferencesClass.getInstance().getInt("first_ad_hide", 0);
    }

    public static void setFirst_ad_hide(int i) {
        SharedPreferencesClass.getInstance().setInt("first_ad_hide", i);
    }

    public static int getIsPreloadSplashAd() {
        return SharedPreferencesClass.getInstance().getInt("isPreloadSplashAd", 0);
    }

    public static void setIsPreloadSplashAd(int i) {
        SharedPreferencesClass.getInstance().setInt("isPreloadSplashAd", i);
    }

    public static int getIsBannerSpaceVisible() {
        return SharedPreferencesClass.getInstance().getInt("isBannerSpaceVisible", 0);
    }

    public static void setIsBannerSpaceVisible(int i) {
        SharedPreferencesClass.getInstance().setInt("isBannerSpaceVisible", i);
    }

    public static int getIsOnLoadNative() {
        return SharedPreferencesClass.getInstance().getInt("isOnLoadNative", 0);
    }

    public static void setIsOnLoadNative(int i) {
        SharedPreferencesClass.getInstance().setInt("isOnLoadNative", i);
    }
public static int getIsOnLoadNativeSmall() {
        return SharedPreferencesClass.getInstance().getInt("isOnLoadNativeSmall", 0);
    }

    public static void setIsOnLoadNativeSmall(int i) {
        SharedPreferencesClass.getInstance().setInt("isOnLoadNativeSmall", i);
    }

    public static int getInterstitialFirstClick() {
        return SharedPreferencesClass.getInstance().getInt("interstitialFirstClick", 3);
    }

    public static void setInterstitialFirstClick(int i) {
        SharedPreferencesClass.getInstance().setInt("interstitialFirstClick", i);
    }

    public static String getPip_sticker() {
        return SharedPreferencesClass.getInstance().getString("pip_sticker", "");
    }

    public static void setPip_sticker(String i) {
        SharedPreferencesClass.getInstance().setString("pip_sticker", i);
    }

    public static String getBase() {
        return SharedPreferencesClass.getInstance().getString("base", "");
    }

    public static void setBase(String i) {
        SharedPreferencesClass.getInstance().setString("base", i);
    }

    public static String getS_Zip() {
        return SharedPreferencesClass.getInstance().getString("s_Zip", "");
    }

    public static void setS_Zip(String i) {
        SharedPreferencesClass.getInstance().setString("s_Zip", i);
    }

    public static String getS_thumb() {
        return SharedPreferencesClass.getInstance().getString("s_thumb", "");
    }

    public static void setS_thumb(String i) {
        SharedPreferencesClass.getInstance().setString("s_thumb", i);
    }

    public static String getS_sticker() {
        return SharedPreferencesClass.getInstance().getString("s_sticker", "");
    }

    public static void setS_sticker(String i) {
        SharedPreferencesClass.getInstance().setString("s_sticker", i);
    }

    public static String getS_frame_back() {
        return SharedPreferencesClass.getInstance().getString("s_frame_back", "");
    }

    public static void setS_frame_back(String i) {
        SharedPreferencesClass.getInstance().setString("s_frame_back", i);
    }

    public static String getS_api() {
        return SharedPreferencesClass.getInstance().getString("s_api", "");
    }

    public static void setS_api(String i) {
        SharedPreferencesClass.getInstance().setString("s_api", i);
    }

    public static int getInstanceCount() {
        int type = SharedPreferencesClass.getInstance().getInt(instanceCount, 0);
        return type;
    }

    public static void setInstanceCount(int str) {
        SharedPreferencesClass.getInstance().setInt(instanceCount, str);
    }


    public static int getIsGDPROn() {
        return SharedPreferencesClass.getInstance().getInt("isGDPROn", 0);
    }

    public static void setIsGDPROn(int i) {
        SharedPreferencesClass.getInstance().setInt("isGDPROn", i);
    }

    public static int getIsGDPROnFailed() {
        return SharedPreferencesClass.getInstance().getInt("isGDPROnFailed", 0);
    }

    public static void setIsGDPROnFailed(int i) {
        SharedPreferencesClass.getInstance().setInt("isGDPROnFailed", i);
    }


}