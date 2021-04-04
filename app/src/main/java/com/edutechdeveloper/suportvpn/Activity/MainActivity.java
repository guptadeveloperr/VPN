 package com.edutechdeveloper.suportvpn.Activity;

 import android.app.Activity;
 import android.content.ActivityNotFoundException;
 import android.content.Intent;
 import android.net.Uri;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.MenuItem;
 import android.view.View;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.RelativeLayout;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.ActionBarDrawerToggle;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.cardview.widget.CardView;
 import androidx.core.view.GravityCompat;
 import androidx.drawerlayout.widget.DrawerLayout;
 import androidx.multidex.BuildConfig;

 import com.anchorfree.reporting.TrackingConstants;
 import com.anchorfree.sdk.SessionConfig;
 import com.anchorfree.sdk.SessionInfo;
 import com.anchorfree.sdk.UnifiedSDK;
 import com.anchorfree.sdk.exceptions.PartnerApiException;
 import com.anchorfree.sdk.rules.TrafficRule;
 import com.anchorfree.vpnsdk.callbacks.Callback;
 import com.anchorfree.vpnsdk.callbacks.CompletableCallback;
 import com.anchorfree.vpnsdk.callbacks.VpnStateListener;
 import com.anchorfree.vpnsdk.exceptions.NetworkRelatedException;
 import com.anchorfree.vpnsdk.exceptions.VpnException;
 import com.anchorfree.vpnsdk.exceptions.VpnPermissionDeniedException;
 import com.anchorfree.vpnsdk.exceptions.VpnPermissionRevokedException;
 import com.anchorfree.vpnsdk.transporthydra.HydraTransport;
 import com.anchorfree.vpnsdk.transporthydra.HydraVpnTransportException;
 import com.anchorfree.vpnsdk.vpnservice.VPNState;
 import com.anjlab.android.iab.v3.BillingProcessor;
 import com.anjlab.android.iab.v3.TransactionDetails;
 import com.edutechdeveloper.suportvpn.AdManager.MyAd;
 import com.edutechdeveloper.suportvpn.Helper.Config;
 import com.edutechdeveloper.suportvpn.MyApplication;
 import com.edutechdeveloper.suportvpn.R;
 import com.google.android.gms.ads.AdView;
 import com.google.android.material.navigation.NavigationView;
 import com.northghost.caketube.CaketubeTransport;
 import com.pixplicity.easyprefs.library.Prefs;

 import java.util.ArrayList;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.Locale;

 import pl.bclogic.pulsator4droid.library.PulsatorLayout;

 import static com.edutechdeveloper.suportvpn.AdManager.AdMob.transporTConfigList;


 public class MainActivity extends AppCompatActivity implements VpnStateListener, BillingProcessor.IBillingHandler, NavigationView.OnNavigationItemSelectedListener{

  //   private Button btn_activation;
    private String selectedCountry;
    private String selectedCountryName;

     private LinearLayout ll_subscription;
     private CardView cv_country_selected, cv_activation;
     private ImageView ib_country_flag, ib_activation, ib_dower;
     private TextView txt_country, txt_status;
     private RelativeLayout rl_activation;
     private PulsatorLayout pulsator;
     protected static final String TAG = MainActivity.class.getSimpleName();

     private boolean isActivateButton = true;

     final int SUBSCRIPTION_ACTIVITY = 1100;
     final int COUNTRY_SELECTED_ACTIVITY = 1200;
     final int RATING_ACTIVITY = 1300;
     final int WELCOME_ACTIVITY = 1400;

     /*google IAP*/
     private BillingProcessor bp;
     private boolean isBPavailable = false;

     private DrawerLayout drawer;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        checkSubscription();
        event();

        MyAd.init(this);



        setupDrawer();


    }

     private void checkSubscription() {
         AdView adView = findViewById(R.id.adView);
         LinearLayout fbAd = findViewById(R.id.banner_container);
         if (Prefs.getBoolean("appFailure", false)){
             ll_subscription.setVisibility(View.GONE);
             adView.setVisibility(View.GONE);
             fbAd.setVisibility(View.GONE);
         }else {
             ll_subscription.setVisibility(View.VISIBLE);
             adView.setVisibility(View.VISIBLE);
             fbAd.setVisibility(View.VISIBLE);
         }
     }

     private void setupDrawer() {
         drawer =  findViewById(R.id.drawer_layout);
         ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                 MainActivity.this, drawer, null, 0, 0);//R.string.navigation_drawer_open, R.string.navigation_drawer_close);
         drawer.addDrawerListener(toggle);
         toggle.syncState();

         NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(MainActivity.this);
     }


     private void event() {
        cv_activation.setOnClickListener(view -> vpnActivation());
        cv_country_selected.setOnClickListener(view -> MyApplication.unifiedSDK.getBackend().isLoggedIn(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean isLoggedIn) {
                if (isActivateButton){
                    if (isLoggedIn) {
                        Intent i = new Intent(MainActivity.this, CountryListActivity.class);
                        startActivityForResult(i, COUNTRY_SELECTED_ACTIVITY);
                    } else {
                        showMessage("Login please");
                    }
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                handleError(e);
            }
        }));

         ll_subscription.setOnClickListener(view -> {

             Intent i = new Intent(MainActivity.this, SubscriptionActivity.class);
             startActivityForResult(i, SUBSCRIPTION_ACTIVITY);
         });

         ib_dower.setOnClickListener(view -> drawer.openDrawer(GravityCompat.START));


     }
     private void vpnActivation() {

         UnifiedSDK.getVpnState(new Callback<VPNState>() {
             @Override
             public void success(@NonNull VPNState vpnState) {
                 if (vpnState == VPNState.CONNECTED) {
                     disconnectToVpn();
                 } else {
                     connectToVpn();
                 }
             }

             @Override
             public void failure(@NonNull VpnException e) {
                 Toast.makeText(MainActivity.this, "VPN Check failed", Toast.LENGTH_SHORT).show();
             }
         });
     }
     private void disconnectToVpn() {
         MyApplication.unifiedSDK.getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
             @Override
             public void complete() {
                 //btn_activation.setText("Active");
                 Toast.makeText(MainActivity.this, "VPN Disconnect", Toast.LENGTH_SHORT).show();
                 MyAd.show(MainActivity.this);


             }

             @Override
             public void error(VpnException e) {
                 handleError(e);

             }
         });
     }
     private void connectToVpn() {
         MyApplication.unifiedSDK.getBackend().isLoggedIn(new Callback<Boolean>() {
             @Override
             public void success(@NonNull Boolean isLoggedIn) {
                 if (isLoggedIn) {

                     List<String> fallbackOrder = new ArrayList<>();
                     fallbackOrder.add(HydraTransport.TRANSPORT_ID);
                     fallbackOrder.add(CaketubeTransport.TRANSPORT_ID_TCP);
                     fallbackOrder.add(CaketubeTransport.TRANSPORT_ID_UDP);

                     List<String> bypassDomains = new LinkedList<>();
                     //bypassDomains.add("*facebook.com");
                     bypassDomains.add("*wtfismyip.com");

                     MyApplication.unifiedSDK.getVPN().start(new SessionConfig.Builder()
                             .withReason(TrackingConstants.GprReasons.M_UI)
                             .withTransportFallback(fallbackOrder)
                             .withTransport(HydraTransport.TRANSPORT_ID)
                             .withVirtualLocation(selectedCountry)
                             .addDnsRule(TrafficRule.Builder.bypass().fromDomains(bypassDomains))
                             .build(), new CompletableCallback() {
                         @Override
                         public void complete() {
                             //btn_activation.setText("Deactive");
                             Toast.makeText(MainActivity.this, "VPN Connected", Toast.LENGTH_SHORT).show();

                             Prefs.putInt("countConnection", Prefs.getInt("countConnection", 0)+1);

                             if (!Prefs.getBoolean("isRate", false) &&
                                     Prefs.getInt("countConnection", 0) == 2 &&
                                      Prefs.getInt("countConnection", 0) == 5 &&
                                      Prefs.getInt("countConnection", 0) == 7){

                                 Intent i = new Intent(MainActivity.this, RatingActivity.class);
                                 startActivityForResult(i, RATING_ACTIVITY);
                             }


                             MyAd.show(MainActivity.this);
                         }

                         @Override
                         public void error(@NonNull VpnException e) {
                             Toast.makeText(MainActivity.this, "Error Connecting", Toast.LENGTH_SHORT).show();

                         }
                     });
                 } else {
                     Toast.makeText(MainActivity.this, "Restart DORA VPN", Toast.LENGTH_SHORT).show();

                 }

             }

             @Override
             public void failure(@NonNull VpnException e) {
                 handleError(e);


             }
         });


     }
     public void handleError(Throwable e) {
         Log.w(TAG, e);
         if (e instanceof NetworkRelatedException) {
             showMessage("Check internet connection");
         } else if (e instanceof VpnException) {
             if (e instanceof VpnPermissionRevokedException) {
                 showMessage("User revoked vpn permissions");
             } else if (e instanceof VpnPermissionDeniedException) {
                 showMessage("User canceled to grant vpn permissions");
             } else if (e instanceof HydraVpnTransportException) {
                 HydraVpnTransportException hydraVpnTransportException = (HydraVpnTransportException) e;
                 if (hydraVpnTransportException.getCode() == HydraVpnTransportException.HYDRA_ERROR_BROKEN) {
                     showMessage("Connection with vpn server was lost");
                 } else if (hydraVpnTransportException.getCode() == HydraVpnTransportException.HYDRA_DCN_BLOCKED_BW) {
                     showMessage("Client traffic exceeded");
                 } else {
                     showMessage("Error in VPN transport");
                 }
             } else {
                 showMessage("Error in VPN Service");
             }
         } else if (e instanceof PartnerApiException) {
             switch (((PartnerApiException) e).getContent()) {
                 case PartnerApiException.CODE_NOT_AUTHORIZED:
                     showMessage("User unauthorized");
                     break;
                 case PartnerApiException.CODE_TRAFFIC_EXCEED:
                     showMessage("Server unavailable");
                     break;
                 default:
                     showMessage("Other error. Check PartnerApiException constants");
                     break;
             }
         }
     }

     private void showMessage(String s) {
         Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
     }

     private void init() {
         ll_subscription = findViewById(R.id.ll_subscription);
         cv_country_selected = findViewById(R.id.cv_country_selected);
         ib_country_flag = findViewById(R.id.ib_country_flag);
         txt_country = findViewById(R.id.txt_country);
         txt_status = findViewById(R.id.txt_status);
         ib_activation = findViewById(R.id.ib_activation);
         rl_activation = findViewById(R.id.rl_activation);
         cv_activation = findViewById(R.id.cv_activation);
         pulsator = findViewById(R.id.pulsator);
         ib_dower = findViewById(R.id.ib_dower);
     }

     @Override
     public void vpnStateChanged(@NonNull VPNState vpnState) {
         updateUI();
     }

     private void updateUI() {
         UnifiedSDK.getVpnState(new Callback<VPNState>() {
             @Override
             public void success(@NonNull VPNState vpnState) {

                 switch (vpnState) {
                     case IDLE: {
                         pulsator.stop();
                         txt_status.setText("Not Connected");
                         rl_activation.setBackground(getResources().getDrawable(R.drawable.activation_disable_bg));
                         isActivateButton = true;
                         break;
                     }
                     case CONNECTED: {
                         pulsator.stop();
                         txt_status.setText("Connected");
                         rl_activation.setBackground(getResources().getDrawable(R.drawable.activation_bg));
                         isActivateButton = true;
                         break;
                     }
                     case CONNECTING_VPN:
                         txt_status.setText("Checking VPN");
                         rl_activation.setClickable(false);
                         rl_activation.setBackground(getResources().getDrawable(R.drawable.activation_bg));
                         isActivateButton = false;

                     case CONNECTING_CREDENTIALS:
                         txt_status.setText("Checking Credentials");
                         isActivateButton = false;

                     case CONNECTING_PERMISSIONS: {
                         txt_status.setText("Connecting VPN");
                         isActivateButton = false;

                        pulsator.start();
                         break;
                     }
                     case PAUSED: {
                         txt_status.setText("Paused");
                         rl_activation.setBackground(getResources().getDrawable(R.drawable.activation_bg));
                         isActivateButton = true;
                         break;
                     }
                 }
             }

             @Override
             public void failure(@NonNull VpnException e) {
                 pulsator.stop();
                 rl_activation.setClickable(true);
                 txt_status.setText("VPN can't connect, please try again..");
                 rl_activation.setBackground(getResources().getDrawable(R.drawable.activation_disable_bg));
             }
         });
         MyApplication.unifiedSDK.getBackend().isLoggedIn(new Callback<Boolean>() {
             @Override
             public void success(@NonNull Boolean isLoggedIn) {

                 //make connect button enabled
             }

             @Override
             public void failure(@NonNull VpnException e) {

             }
         });
         UnifiedSDK.getVpnState(new Callback<VPNState>() {
             @Override
             public void success(@NonNull VPNState state) {
                 if (state == VPNState.CONNECTED) {
                     UnifiedSDK.getStatus(new Callback<SessionInfo>() {
                         @Override
                         public void success(@NonNull SessionInfo sessionInfo) {
                             runOnUiThread(() -> {
                                 txt_country.setText(selectedCountryName != null ? selectedCountryName : "UNKNOWN");
                                 ib_country_flag.setImageResource(MainActivity.this.getResources().getIdentifier("drawable/" + selectedCountry, "drawable", MainActivity.this.getPackageName()));
                             });

                         }

                         @Override
                         public void failure(@NonNull VpnException e) {
                             runOnUiThread(() -> {
                                 txt_country.setText(selectedCountryName != null ? selectedCountryName : "UNKNOWN");
                                 ib_country_flag.setImageResource(MainActivity.this.getResources().getIdentifier("drawable/" + selectedCountry, "drawable", MainActivity.this.getPackageName()));
                             });
                         }
                     });
                 } else {
                     runOnUiThread(() -> {
                         txt_country.setText(selectedCountryName != null ? selectedCountryName : "UNKNOWN");
                         ib_country_flag.setImageResource(MainActivity.this.getResources().getIdentifier("drawable/" + selectedCountry, "drawable", MainActivity.this.getPackageName()));

                     });
                 }
             }

             @Override
             public void failure(@NonNull VpnException e) {
                 txt_country.setText("Select a country");
                 ib_country_flag.setImageDrawable(getResources().getDrawable(R.drawable.select_flag_image));
             }
         });




     }

     @Override
     public void vpnError(@NonNull VpnException e) {
         updateUI();
         handleError(e);
     }

     @Override
     protected void onStart() {
         super.onStart();
         Locale locale = new Locale("", Prefs.getString("sname", "ca"));

         selectedCountryName = locale.getDisplayCountry();
         selectedCountry = locale.getCountry().toLowerCase();

         UnifiedSDK.addVpnStateListener(this);
         transporTConfigList(this);
         initIAP();
     }


     @Override
     protected void onStop() {
         super.onStop();
         UnifiedSDK.removeVpnStateListener(this);
     }

     @Override
     protected void onDestroy() {
         MyAd.destroy(MainActivity.this);
         if (bp != null) {
             bp.release();
         }
         super.onDestroy();
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == SUBSCRIPTION_ACTIVITY) {
             if(resultCode == Activity.RESULT_OK){
                 String subscription=data.getStringExtra("subscription");
                 bp.subscribe(MainActivity.this, subscription);

             }
         }else if(requestCode == COUNTRY_SELECTED_ACTIVITY){
             if(resultCode == Activity.RESULT_OK){
                 String country = data.getStringExtra("country").toLowerCase();

                 //last time selected servers
                 Locale locale = new Locale("", country);
                 txt_country.setText(locale.getDisplayCountry());
                 ib_country_flag.setImageResource(MainActivity.this.getResources().getIdentifier("drawable/" + country, "drawable", MainActivity.this.getPackageName()));
                 // str_country = selectedServerTextView.getText().toString();

                 selectedCountryName = locale.getDisplayCountry();
                 selectedCountry = country;
                 updateUI();

               UnifiedSDK.getVpnState(new Callback<VPNState>() {
                     @Override
                     public void success(@NonNull VPNState state) {
                         if (state == VPNState.CONNECTED) {
                             showMessage("Reconnecting to VPN with ");
                             txt_status.setText("Reconnecting to VPN");
                             MyApplication.unifiedSDK.getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
                                 @Override
                                 public void complete() {
                                     connectToVpn();
                                 }

                                 @Override
                                 public void error(VpnException e) {
                                     // In this case we try to reconnect
                                     selectedCountry = "";
                                     connectToVpn();
                                     handleError(e);
                                 }
                             });
                         }
                     }

                     @Override
                     public void failure(@NonNull VpnException e) {
                         handleError(e);
                     }
                 });
             }
         }else if (!bp.handleActivityResult(requestCode, resultCode, data)) {
             super.onActivityResult(requestCode, resultCode, data);
         }else if (requestCode == WELCOME_ACTIVITY) {
             if(resultCode == Activity.RESULT_OK){
                 String subscription=data.getStringExtra("subscription");
                 bp.subscribe(MainActivity.this, subscription);
             }
         }
     }

     @Override
     public void onProductPurchased(String productId, TransactionDetails details) {
         Toast.makeText(this, "Subscribed Successfully", Toast.LENGTH_SHORT).show();
         Prefs.putBoolean("appFailure", true);
         Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         startActivity(intent);
         finish();

     }

     @Override
     public void onPurchaseHistoryRestored() {
         Prefs.putBoolean("appFailure", true);
     }

     @Override
     public void onBillingError(int errorCode, Throwable error) {
         Toast.makeText(this, "Billing Error", Toast.LENGTH_SHORT).show();
         Prefs.putBoolean("appFailure", false);
     }

     @Override
     public void onBillingInitialized() {
         if (BillingProcessor.isIabServiceAvailable(MainActivity.this)) {
             isBPavailable = true;
         }
         if (bp.isSubscribed(Config.remove_ads_one_month)  || bp.isSubscribed(Config.remove_ads_six_month) || bp.isSubscribed(Config.remove_ads_one_year)) {
             Config.ads_subscription = true;
             Prefs.putBoolean("appFailure", true);

             if (Prefs.getBoolean("isFirstTime", true)){
                 Prefs.putBoolean("isFirstTime", false);

                 Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(intent);
                 finish();

             }
         }else {
             Prefs.putBoolean("appFailure", false);
             if (Prefs.getBoolean("isFirstTime", true)){
                 Prefs.putBoolean("isFirstTime", false);

                 Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
                 startActivityForResult(i, WELCOME_ACTIVITY);

             }

         }
     }


     private void initIAP() {
         bp = new BillingProcessor(MainActivity.this, Config.lisence_key, this);
         bp.initialize();
     }

     @Override
     public boolean onNavigationItemSelected(MenuItem item) {
         int id = item.getItemId();

         if (id == R.id.nav_upgrade) {
             Intent i = new Intent(MainActivity.this, CountryListActivity.class);
             startActivityForResult(i, COUNTRY_SELECTED_ACTIVITY);
         } else if (id == R.id.nav_unlock) {
             if (Config.ads_subscription){
                 Toast.makeText(this, "You're already taken subscription..", Toast.LENGTH_SHORT).show();
             }else {
                 Intent i = new Intent(MainActivity.this, SubscriptionActivity.class);
                 startActivityForResult(i, SUBSCRIPTION_ACTIVITY);
             }
         } else if (id == R.id.nav_helpus) {
             Intent intent = new Intent(Intent.ACTION_SENDTO);
             intent.setData(Uri.parse("mailto:"));
             intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"edutechdeveloper@gmail.com"});
             intent.putExtra(Intent.EXTRA_SUBJECT, "Improve Comments");
             intent.putExtra(Intent.EXTRA_TEXT, "message body");

             try {
                 startActivity(Intent.createChooser(intent, "send mail"));
             } catch (ActivityNotFoundException ex) {
                 Toast.makeText(this, "No mail app found!!!", Toast.LENGTH_SHORT);
             } catch (Exception ex) {
                 Toast.makeText(this, "Unexpected Error!!!", Toast.LENGTH_SHORT);
             }
         } else if (id == R.id.nav_rate) {
             Intent i = new Intent(MainActivity.this, RatingActivity.class);
             startActivityForResult(i, RATING_ACTIVITY);
         } else if (id == R.id.nav_share) {
             try {
                 Intent shareIntent = new Intent(Intent.ACTION_SEND);
                 shareIntent.setType("text/plain");
                 shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share app");
                 shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm using this Free VPN App, it's provide all servers free https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                 startActivity(Intent.createChooser(shareIntent, "choose one"));
             } catch (Exception e) {
             }
         }else if (id == R.id.nav_policy) {
             Uri uri = Uri.parse(getResources().getString(R.string.privacy_policy_link)); // missing 'http://' will cause crashed
             Intent intent = new Intent(Intent.ACTION_VIEW, uri);
             startActivity(intent);
         }
         drawer.closeDrawer(GravityCompat.START);
         return true;
     }

 }
