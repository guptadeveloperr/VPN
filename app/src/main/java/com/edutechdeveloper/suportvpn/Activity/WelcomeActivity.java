package com.edutechdeveloper.suportvpn.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.edutechdeveloper.suportvpn.Helper.Config;
import com.edutechdeveloper.suportvpn.R;

public class WelcomeActivity extends AppCompatActivity {

    private ImageButton ib_close;
    private Button btn_try_free, btn_subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init();
        event();

    }

    private void event() {
        ib_close.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        });

        btn_try_free.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        });
        btn_subscription.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("subscription", Config.remove_ads_one_month);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        });
    }

    private void init() {
        ib_close = findViewById(R.id.ib_close);
        btn_try_free = findViewById(R.id.btn_try_free);
        btn_subscription = findViewById(R.id.btn_subscription);

    }


}

