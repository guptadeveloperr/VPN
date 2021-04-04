package com.edutechdeveloper.suportvpn.AdManager;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.edutechdeveloper.suportvpn.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class Fan {
    private static InterstitialAd interstitialAd;
    private static AdView adView;
    private static boolean isRequestShowAd = false;

    public static void init(Activity activity){
        interstitialAd = new InterstitialAd(activity, activity.getString(R.string.fan_interstitial_id));
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                isRequestShowAd = false;
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                if (isRequestShowAd){
                    isRequestShowAd = false;
                    interstitialAd.show();
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        };
        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());


        adView = new AdView(activity, activity.getString(R.string.fan_banner_id), AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = activity.findViewById(R.id.banner_container);
        adContainer.setVisibility(View.VISIBLE);
        adContainer.addView(adView);
        adView.loadAd();
    }

    public static void destroy(){
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        if (adView != null){
            adView.destroy();
        }
    }

    public static void show(){
        if (interstitialAd.isAdLoaded()){
            interstitialAd.show();
        }else {
            isRequestShowAd = true;
        }
    }


}
