package com.edutechdeveloper.suportvpn.AdManager;

import android.app.Activity;
import android.content.Context;

import com.edutechdeveloper.suportvpn.R;
import com.pixplicity.easyprefs.library.Prefs;

public class MyAd {

    public static void init(Activity activity){

        if (!Prefs.getBoolean("appFailure", false)) {
            if (activity.getResources().getInteger(R.integer.ad_type) == 2) {
                Fan.init(activity);
            } else {
                AdMob.init(activity);
            }
        }
    }

    public static void show(Context context){
        if (!Prefs.getBoolean("appFailure", false)){
            if (context.getResources().getInteger(R.integer.ad_type) == 2) {
                Fan.show();
            }else {
                AdMob.show();
            }
        }
    }
    public static void destroy(Context context){
        if (context.getResources().getInteger(R.integer.ad_type) == 2) {
            Fan.destroy();
        }
    }
}
