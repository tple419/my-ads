package com.module.adsdk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdsModel {

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("isPreloadSplashAd")
    @Expose
    private int isPreloadSplashAd = 0;

    @SerializedName("isOnLoadNative")
    @Expose
    private int isOnLoadNative = 0;
@SerializedName("isBannerSpaceVisible")
    @Expose
    private int isBannerSpaceVisible = 0;

    @SerializedName("AdShowStatus")
    @Expose
    private int adShow_adstatus;

    @SerializedName("AdmobAdStatus")
    @Expose
    private int admob_adstatus;

    @SerializedName("AdXAdStatus")
    @Expose
    private int adX_adstatus;

    @SerializedName("ApplovinAdStatus")
    @Expose
    private int appLovin_adstatus;

    @SerializedName("FBAdStatus")
    @Expose
    private int fb_adstatus;

    @SerializedName("IronSourceAdStatus")
    @Expose
    private int ironSource_adstatus;

    @SerializedName("bannerAdStatus")
    @Expose
    private int banner_adstatus;

    @SerializedName("nativeAdStatus")
    @Expose
    private int native_adstatus;

    @SerializedName("interAdStatus")
    @Expose
    private int inter_adstatus;

    @SerializedName("appOpenAdStatus")
    @Expose
    private int appOpen_adstatus;

    @SerializedName("appOpenPerSession")
    @Expose
    private int appOpen_count;

    @SerializedName("fullScreenAdsPerSession")
    @Expose
    private int fullScreen_count;

    @SerializedName("interstitialCount")
    @Expose
    private int interstitial_count;

    @SerializedName("exit_ad_enable")
    @Expose
    private int exitAdEnable;

    @SerializedName("splash_ad_type")
    @Expose
    private int splash_ad_type;

    @SerializedName("is_splash_on")
    @Expose
    private int is_splash_on;
    @SerializedName("splash_time")
    @Expose
    private int splash_time;

    @SerializedName("AdmobBannerId")
    @Expose
    private String Admob_bannerid;

    @SerializedName("AdmobNativeId")
    @Expose
    private String Admob_nativeid;

    @SerializedName("AdmobInterId")
    @Expose
    private String Admob_interid;

    @SerializedName("AdmobAppOpenId")
    @Expose
    private String Admob_appopenid;


    @SerializedName("AdxBannerId")
    @Expose
    private String Adx_bannerid;

    @SerializedName("AdxNativeId")
    @Expose
    private String Adx_nativeid;

    @SerializedName("AdxInterId")
    @Expose
    private String Adx_interid;

    @SerializedName("AdxAppOpenId")
    @Expose
    private String Adx_appopenid;


    @SerializedName("applovinBanner")
    @Expose
    private String applovin_bannerid;

    @SerializedName("applovinNative")
    @Expose
    private String applovin_nativeid;

    @SerializedName("applovinInter")
    @Expose
    private String applovin_interid;

    @SerializedName("applovinOpenAdId")
    @Expose
    private String applovin_OpenAdId;


    @SerializedName("FBBanner")
    @Expose
    private String FBBanner;

    @SerializedName("FBNative")
    @Expose
    private String FBNative;

    @SerializedName("FBInter")
    @Expose
    private String FBInter;


    @SerializedName("ironappkey")
    @Expose
    private String ironappkey;

    @SerializedName("bannerSequence")
    @Expose
    private String banner_sequence;

    @SerializedName("nativeSequence")
    @Expose
    private String native_sequence;

    @SerializedName("InterSequence")
    @Expose
    private String inter_sequence;
    @SerializedName("splashAdsSequence")
    @Expose
    private String splashAdsSequence;

    @SerializedName("AppOpenSequence")
    @Expose
    private String AppOpen_sequence;

    @SerializedName("app_versionCode")
    @Expose
    private String app_versionCode;

    @SerializedName("app_redirectOtherAppStatus")
    @Expose
    private int app_redirectOtherAppStatus;

    @SerializedName("app_newPackageName")
    @Expose
    private String app_newPackageName;

    @SerializedName("privacy_policy")
    @Expose
    private String app_policy_url;

    @SerializedName("email")
    @Expose
    private String app_email_id;

    /*new fields*/

    @SerializedName("native_display_list")
    @Expose
    private int native_display_list;

    @SerializedName("banner_display_list")
    @Expose
    private int banner_display_list;

    @SerializedName("banner_display_pager")
    @Expose
    private int banner_display_pager;

    @SerializedName("review_popup_count")
    @Expose
    private int review_popup_count;

    @SerializedName("direct_review_enable")
    @Expose
    private int direct_review_enable;

    @SerializedName("album_click_enabled")
    @Expose
    private int album_click_enabled;

    @SerializedName("in_appreview")
    @Expose
    private int in_appreview;

    @SerializedName("review")
    @Expose
    private int review;

    @SerializedName("isactive")
    @Expose
    private int isactive;

    @SerializedName("first_ad_hide")
    @Expose
    private int first_ad_hide;


    @SerializedName("pip_sticker")
    @Expose
    private String pip_sticker;

    @SerializedName("base")
    @Expose
    private String base;

    @SerializedName("s_Zip")
    @Expose
    private String s_Zip;

    @SerializedName("s_thumb")
    @Expose
    private String s_thumb;

    @SerializedName("s_sticker")
    @Expose
    private String s_sticker;

    @SerializedName("s_frame_back")
    @Expose
    private String s_frame_back;

    @SerializedName("s_api")
    @Expose
    private String s_api;
    @SerializedName("app_updateAppDialogStatus")
    @Expose
    private int app_updateAppDialogStatus;

    public int getNative_display_list() {
        return native_display_list;
    }

    public void setNative_display_list(int native_display_list) {
        this.native_display_list = native_display_list;
    }

    public int getBanner_display_list() {
        return banner_display_list;
    }

    public void setBanner_display_list(int banner_display_list) {
        this.banner_display_list = banner_display_list;
    }

    public int getBanner_display_pager() {
        return banner_display_pager;
    }

    public void setBanner_display_pager(int banner_display_pager) {
        this.banner_display_pager = banner_display_pager;
    }

    public int getReview_popup_count() {
        return review_popup_count;
    }

    public void setReview_popup_count(int review_popup_count) {
        this.review_popup_count = review_popup_count;
    }

    public int getDirect_review_enable() {
        return direct_review_enable;
    }

    public void setDirect_review_enable(int direct_review_enable) {
        this.direct_review_enable = direct_review_enable;
    }

    public int getAlbum_click_enabled() {
        return album_click_enabled;
    }

    public void setAlbum_click_enabled(int album_click_enabled) {
        this.album_click_enabled = album_click_enabled;
    }

    public int getIn_appreview() {
        return in_appreview;
    }

    public void setIn_appreview(int in_appreview) {
        this.in_appreview = in_appreview;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public int getIsactive() {
        return isactive;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }

    public int getFirst_ad_hide() {
        return first_ad_hide;
    }

    public void setFirst_ad_hide(int first_ad_hide) {
        this.first_ad_hide = first_ad_hide;
    }

    public String getPip_sticker() {
        return pip_sticker;
    }

    public void setPip_sticker(String pip_sticker) {
        this.pip_sticker = pip_sticker;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getS_Zip() {
        return s_Zip;
    }

    public void setS_Zip(String s_Zip) {
        this.s_Zip = s_Zip;
    }

    public String getS_thumb() {
        return s_thumb;
    }

    public void setS_thumb(String s_thumb) {
        this.s_thumb = s_thumb;
    }

    public String getS_sticker() {
        return s_sticker;
    }

    public void setS_sticker(String s_sticker) {
        this.s_sticker = s_sticker;
    }

    public String getS_frame_back() {
        return s_frame_back;
    }

    public void setS_frame_back(String s_frame_back) {
        this.s_frame_back = s_frame_back;
    }

    public String getS_api() {
        return s_api;
    }

    public void setS_api(String s_api) {
        this.s_api = s_api;
    }

    public String getApp_policy_url() {
        return app_policy_url;
    }

    public void setApp_policy_url(String app_policy_url) {
        this.app_policy_url = app_policy_url;
    }

    public String getApp_email_id() {
        return app_email_id;
    }

    public void setApp_email_id(String app_email_id) {
        this.app_email_id = app_email_id;
    }

    public String getApp_versionCode() {
        return app_versionCode;
    }

    public void setApp_versionCode(String app_versionCode) {
        this.app_versionCode = app_versionCode;
    }

    public int getApp_redirectOtherAppStatus() {
        return app_redirectOtherAppStatus;
    }

    public void setApp_redirectOtherAppStatus(int app_redirectOtherAppStatus) {
        this.app_redirectOtherAppStatus = app_redirectOtherAppStatus;
    }

    public String getApp_newPackageName() {
        return app_newPackageName;
    }

    public void setApp_newPackageName(String app_newPackageName) {
        this.app_newPackageName = app_newPackageName;
    }

    public int getApp_updateAppDialogStatus() {
        return app_updateAppDialogStatus;
    }

    public void setApp_updateAppDialogStatus(int app_updateAppDialogStatus) {
        this.app_updateAppDialogStatus = app_updateAppDialogStatus;
    }

    public String getAppOpen_sequence() {
        return AppOpen_sequence;
    }

    public void setAppOpen_sequence(String appOpen_sequence) {
        AppOpen_sequence = appOpen_sequence;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdShow_adstatus() {
        return adShow_adstatus;
    }

    public void setAdShow_adstatus(int adShow_adstatus) {
        this.adShow_adstatus = adShow_adstatus;
    }

    public int getAdmob_adstatus() {
        return admob_adstatus;
    }

    public void setAdmob_adstatus(int admob_adstatus) {
        this.admob_adstatus = admob_adstatus;
    }

    public int getAdX_adstatus() {
        return adX_adstatus;
    }

    public void setAdX_adstatus(int adX_adstatus) {
        this.adX_adstatus = adX_adstatus;
    }

    public int getAppLovin_adstatus() {
        return appLovin_adstatus;
    }

    public void setAppLovin_adstatus(int appLovin_adstatus) {
        this.appLovin_adstatus = appLovin_adstatus;
    }

    public int getIronSource_adstatus() {
        return ironSource_adstatus;
    }

    public void setIronSource_adstatus(int ironSource_adstatus) {
        this.ironSource_adstatus = ironSource_adstatus;
    }

    public int getBanner_adstatus() {
        return banner_adstatus;
    }

    public void setBanner_adstatus(int banner_adstatus) {
        this.banner_adstatus = banner_adstatus;
    }

    public int getNative_adstatus() {
        return native_adstatus;
    }

    public void setNative_adstatus(int native_adstatus) {
        this.native_adstatus = native_adstatus;
    }

    public int getInter_adstatus() {
        return inter_adstatus;
    }

    public void setInter_adstatus(int inter_adstatus) {
        this.inter_adstatus = inter_adstatus;
    }

    public int getAppOpen_adstatus() {
        return appOpen_adstatus;
    }

    public void setAppOpen_adstatus(int appOpen_adstatus) {
        this.appOpen_adstatus = appOpen_adstatus;
    }

    public int getAppOpen_count() {
        return appOpen_count;
    }

    public void setAppOpen_count(int appOpen_count) {
        this.appOpen_count = appOpen_count;
    }

    public int getFullScreen_count() {
        return fullScreen_count;
    }

    public void setFullScreen_count(int fullScreen_count) {
        this.fullScreen_count = fullScreen_count;
    }

    public int getInterstitial_count() {
        return interstitial_count;
    }

    public void setInterstitial_count(int interstitial_count) {
        this.interstitial_count = interstitial_count;
    }


    public int getFb_adstatus() {
        return fb_adstatus;
    }

    public void setFb_adstatus(int fb_adstatus) {
        this.fb_adstatus = fb_adstatus;
    }

    public String getFBBanner() {
        return FBBanner;
    }

    public void setFBBanner(String FBBanner) {
        this.FBBanner = FBBanner;
    }

    public String getFBNative() {
        return FBNative;
    }

    public void setFBNative(String FBNative) {
        this.FBNative = FBNative;
    }

    public String getFBInter() {
        return FBInter;
    }

    public void setFBInter(String FBInter) {
        this.FBInter = FBInter;
    }

    public int getExitAdEnable() {
        return exitAdEnable;
    }

    public void setExitAdEnable(int exitAdEnable) {
        this.exitAdEnable = exitAdEnable;
    }

    public String getAdmob_bannerid() {
        return Admob_bannerid;
    }

    public void setAdmob_bannerid(String admob_bannerid) {
        Admob_bannerid = admob_bannerid;
    }

    public String getAdmob_nativeid() {
        return Admob_nativeid;
    }

    public void setAdmob_nativeid(String admob_nativeid) {
        Admob_nativeid = admob_nativeid;
    }

    public String getAdmob_interid() {
        return Admob_interid;
    }

    public void setAdmob_interid(String admob_interid) {
        Admob_interid = admob_interid;
    }

    public String getAdmob_appopenid() {
        return Admob_appopenid;
    }

    public void setAdmob_appopenid(String admob_appopenid) {
        Admob_appopenid = admob_appopenid;
    }

    public String getAdx_bannerid() {
        return Adx_bannerid;
    }

    public void setAdx_bannerid(String adx_bannerid) {
        Adx_bannerid = adx_bannerid;
    }

    public String getAdx_nativeid() {
        return Adx_nativeid;
    }

    public void setAdx_nativeid(String adx_nativeid) {
        Adx_nativeid = adx_nativeid;
    }

    public String getAdx_interid() {
        return Adx_interid;
    }

    public void setAdx_interid(String adx_interid) {
        Adx_interid = adx_interid;
    }

    public String getAdx_appopenid() {
        return Adx_appopenid;
    }

    public void setAdx_appopenid(String adx_appopenid) {
        Adx_appopenid = adx_appopenid;
    }

    public String getApplovin_bannerid() {
        return applovin_bannerid;
    }

    public void setApplovin_bannerid(String applovin_bannerid) {
        this.applovin_bannerid = applovin_bannerid;
    }

    public String getApplovin_nativeid() {
        return applovin_nativeid;
    }

    public void setApplovin_nativeid(String applovin_nativeid) {
        this.applovin_nativeid = applovin_nativeid;
    }

    public String getApplovin_interid() {
        return applovin_interid;
    }

    public void setApplovin_interid(String applovin_interid) {
        this.applovin_interid = applovin_interid;
    }

    public String getApplovin_OpenAdId() {
        return applovin_OpenAdId;
    }

    public void setApplovin_OpenAdId(String applovin_OpenAdid) {
        this.applovin_OpenAdId = applovin_OpenAdid;
    }

    public String getIronappkey() {
        return ironappkey;
    }

    public void setIronappkey(String ironappkey) {
        this.ironappkey = ironappkey;
    }

    public String getBanner_sequence() {
        return banner_sequence;
    }

    public void setBanner_sequence(String banner_sequence) {
        this.banner_sequence = banner_sequence;
    }

    public String getNative_sequence() {
        return native_sequence;
    }

    public void setNative_sequence(String native_sequence) {
        this.native_sequence = native_sequence;
    }

    public String getInter_sequence() {
        return inter_sequence;
    }

    public void setInter_sequence(String inter_sequence) {
        this.inter_sequence = inter_sequence;
    }

    public String getSplashAdsSequence() {
        return splashAdsSequence;
    }

    public void setSplashAdsSequence(String splashAdsSequence) {
        this.splashAdsSequence = splashAdsSequence;
    }

    public int getSplash_ad_type() {
        return splash_ad_type;
    }

    public void setSplash_ad_type(int splash_ad_type) {
        this.splash_ad_type = splash_ad_type;
    }

    public int getIs_splash_on() {
        return is_splash_on;
    }

    public void setIs_splash_on(int is_splash_on) {
        this.is_splash_on = is_splash_on;
    }

    public int getSplash_time() {
        return splash_time;
    }

    public int getIsBannerSpaceVisible() {
        return isBannerSpaceVisible;
    }

    public void setIsBannerSpaceVisible(int isBannerSpaceVisible) {
        this.isBannerSpaceVisible = isBannerSpaceVisible;
    }

    public void setSplash_time(int splash_time) {
        this.splash_time = splash_time;
    }

    public int getIsPreloadSplashAd() {
        return isPreloadSplashAd;
    }

    public void setIsPreloadSplashAd(int isPreloadSplashAd) {
        this.isPreloadSplashAd = isPreloadSplashAd;
    }

    public int getIsOnLoadNative() {
        return isOnLoadNative;
    }

    public void setIsOnLoadNative(int isOnLoadNative) {
        this.isOnLoadNative = isOnLoadNative;
    }
}
