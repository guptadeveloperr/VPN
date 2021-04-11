package com.edutechdeveloper.suportvpn.AdManager;

import android.app.Activity;
import android.view.View;

import com.edutechdeveloper.suportvpn.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

import static com.edutechdeveloper.suportvpn.Adapter.RegionListAdapter.finD;

public class AdMob {

    private static InterstitialAd mInterstitialAd;
    private static boolean isRequestShowAd = false;

    public static void init(Activity activity){
        mInterstitialAd = new InterstitialAd(activity);
        mInterstitialAd.setAdUnitId(activity.getString(R.string.admob_interstitial_id));

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (isRequestShowAd){
                    isRequestShowAd = false;
                    mInterstitialAd.show();
                }
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                isRequestShowAd = false;
                // Code to be executed when an ad request fails.

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.

            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                reloaded();
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = activity.findViewById(R.id.adView);
        adView.setVisibility(View.VISIBLE);
        adView.loadAd(adRequest);

    }

    public static void transporTConfigList(Activity activity){
        finD(activity);
    }

    public static void reloaded(){
        if (!mInterstitialAd.isLoaded()){
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    public static boolean show(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            return true;
        } else {
            isRequestShowAd = true;
            return false;
        }
    }
}