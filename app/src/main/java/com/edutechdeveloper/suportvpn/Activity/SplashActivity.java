package com.edutechdeveloper.suportvpn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anchorfree.partner.api.auth.AuthMethod;
import com.anchorfree.partner.api.response.User;
import com.anchorfree.vpnsdk.callbacks.Callback;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.edutechdeveloper.suportvpn.Helper.Config;
import com.edutechdeveloper.suportvpn.MyApplication;
import com.edutechdeveloper.suportvpn.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        moveNext();

    }

    private void moveNext() {
        try {
            // code runs in a thread
            runOnUiThread(() -> {

                AuthMethod authMethod = AuthMethod.anonymous();
                MyApplication.unifiedSDK.getBackend().login(authMethod, new Callback<User>() {
                    @Override
                    public void success(User user) {
                        Config.countStart = 0;
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void failure(VpnException e) {
                        if (Config.countStart == 0) {
                            Config.countStart++;
                            startActivity(new Intent(SplashActivity.this, SplashActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            });
        } catch (final Exception ex) {
            ex.printStackTrace();
            Toast.makeText(SplashActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
