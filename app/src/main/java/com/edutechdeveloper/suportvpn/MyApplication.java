package com.edutechdeveloper.suportvpn;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.util.Log;

import com.anchorfree.partner.api.ClientInfo;
import com.anchorfree.sdk.HydraTransportConfig;
import com.anchorfree.sdk.NotificationConfig;
import com.anchorfree.sdk.TransportConfig;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.vpnsdk.callbacks.CompletableCallback;
import com.edutechdeveloper.suportvpn.Helper.Config;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.northghost.caketube.OpenVpnTransportConfig;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import static com.edutechdeveloper.suportvpn.Helper.Config.appSecretCode;

public class MyApplication extends Application {

    private static final String CHANNEL_ID = "global-vpn-pro";

    private static Context context;


    public static Context getApplication() {
        return context;
    }

    public static Context getStaticContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        // Initialize the Prefs class
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        Prefs.putString("pw1", appSecretCode);

        initHydraSdk();
        initAd();
    }

    private void initAd() {
        if (getResources().getInteger(R.integer.ad_type) == 2){
            // Initialize the Audience Network SDK
            AudienceNetworkAds.initialize(this);
        }else{
            // Initialize the Google Admob SDK
            MobileAds.initialize(this, initializationStatus -> {
            });
        }
    }

   public static UnifiedSDK unifiedSDK;

    public void initHydraSdk() {

        ClientInfo clientInfo = ClientInfo.newBuilder()
                .baseUrl(Config.baseURL)
                .carrierId(Config.carrierID)
                .build();
        unifiedSDK = UnifiedSDK.getInstance(clientInfo);
        UnifiedSDK.clearInstances();


        createNotificationChannel();

        List<TransportConfig> transportConfigList = new ArrayList<>();
        transportConfigList.add(HydraTransportConfig.create());
        transportConfigList.add(OpenVpnTransportConfig.tcp());
        transportConfigList.add(OpenVpnTransportConfig.udp());
        UnifiedSDK.update(transportConfigList, CompletableCallback.EMPTY);

        NotificationConfig notificationConfig = NotificationConfig.newBuilder()
                .title(getResources().getString(R.string.app_name))
                .channelId(CHANNEL_ID)
                .build();
        UnifiedSDK.update(notificationConfig);

        UnifiedSDK.setLoggingLevel(Log.VERBOSE);


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String description = "VPN Active";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getResources().getString(R.string.app_name), importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
