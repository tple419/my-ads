package com.module.adsdk;

import static com.module.adsdk.AppManage.nativeAdSize;
import static com.module.adsdk.AppManage.nativeSmallAdSize;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inflate_ADS {
    Context activity;

    public Inflate_ADS(Context context) {
        this.activity = context;
    }


    public void inflate_NATIV_ADMOB(com.google.android.gms.ads.nativead.NativeAd nativeAd, ViewGroup cardView, boolean isNeedSpace , int nativeViewType) {

        cardView.setVisibility(View.VISIBLE);
        LayoutInflater inflater = LayoutInflater.from(activity);
        NativeAdView adView = null;
        if(nativeViewType  == 1){
            adView =  (NativeAdView) inflater.inflate(R.layout.ads_native_admob_adview, null);
        }else if(nativeViewType  == 2){
            adView =  (NativeAdView) inflater.inflate(R.layout.ads_native_admob_adview_small, null);
        }else {
            adView =  (NativeAdView) inflater.inflate(R.layout.ads_native_admob_adview, null);
        }
        cardView.removeAllViews();

      /*  if(nativeViewType  == 1){
            if (isNeedSpace) {
                final float scale = activity.getResources().getDisplayMetrics().density;
                int pixels = (int) (nativeAdSize * scale + 0.5f);

                cardView.getLayoutParams().height = pixels;
                cardView.requestLayout();
            }

        }else {
            if (isNeedSpace) {
                final float scale = activity.getResources().getDisplayMetrics().density;
                int pixels = (int) (nativeSmallAdSize * scale + 0.5f);

                cardView.getLayoutParams().height = pixels;
                cardView.requestLayout();
            }
        }*/
        cardView.addView(adView);

        if(nativeViewType  == 1){
            adView.setMediaView((com.google.android.gms.ads.nativead.MediaView) adView.findViewById(R.id.ad_media));
        }

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if(nativeViewType  == 1) {
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        }

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }


        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }


        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
                ((RatingBar) adView.getStarRatingView())
                        .setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);

        } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);

        }

        adView.getStoreView().setVisibility(View.GONE);
        adView.getPriceView().setVisibility(View.GONE);

        adView.setNativeAd(nativeAd);
        if(nativeViewType  == 1) {
            VideoController vc = Objects.requireNonNull(nativeAd.getMediaContent()).getVideoController();

            if (vc.hasVideoContent()) {
                vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                    @Override
                    public void onVideoEnd() {
                        super.onVideoEnd();
                    }
                });
            }
        }
    }




    public void inflate_NATIV_FB(NativeAd nativeAd, ViewGroup cardView, boolean isNeedSpace,/* boolean isNativeSmall,*/int nativeViewType) {
        nativeAd.unregisterView();
        cardView.setVisibility(View.VISIBLE);
        LayoutInflater inflater = LayoutInflater.from(activity);
        com.facebook.ads.NativeAdLayout adView = null;
        if(nativeViewType == 1){
            adView = (com.facebook.ads.NativeAdLayout) inflater.inflate(R.layout.ads_native_fb_adview, null);
        }else {
             adView = (com.facebook.ads.NativeAdLayout) inflater.inflate(R.layout.ads_native_fb_adview_small, null);
        }
        cardView.removeAllViews();
        /*if(nativeViewType == 1){
            if (isNeedSpace) {
                final float scale = activity.getResources().getDisplayMetrics().density;
                int pixels = (int) (nativeAdSize * scale + 0.5f);

                cardView.getLayoutParams().height = pixels;
                cardView.requestLayout();
            }

        }else {
            if (isNeedSpace) {
                final float scale = activity.getResources().getDisplayMetrics().density;
                int pixels = (int) (nativeSmallAdSize * scale + 0.5f);

                cardView.getLayoutParams().height = pixels;
                cardView.requestLayout();
            }
        }*/
        cardView.addView(adView);



        // Add the AdOptionsView
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        if (adChoicesContainer != null) {
            AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, adView);
            adChoicesContainer.removeAllViews();
            adChoicesContainer.addView(adOptionsView, 0);
        }



        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = null;
        if(nativeViewType == 1) {
            nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        }
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdIcon);
        if(nativeViewType == 1) {
            clickableViews.add(nativeAdMedia);
        }
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        if(nativeViewType == 1) {
            nativeAd.registerViewForInteraction(
                    adView, nativeAdMedia, nativeAdIcon, clickableViews);

        }else {
            nativeAd.registerViewForInteraction(
                    adView, nativeAdIcon, clickableViews);
        }

    }



}
