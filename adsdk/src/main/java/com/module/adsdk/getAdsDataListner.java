package com.module.adsdk;

public interface getAdsDataListner {

    void onSuccess();
    void onUpdate(String url);

    void onRedirect(String url);

    void onReload();

}
