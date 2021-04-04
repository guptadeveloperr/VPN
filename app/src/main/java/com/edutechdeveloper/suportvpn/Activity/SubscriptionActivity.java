package com.edutechdeveloper.suportvpn.Activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.edutechdeveloper.suportvpn.Helper.Config;
import com.edutechdeveloper.suportvpn.R;

public class SubscriptionActivity extends AppCompatActivity {

    private ImageButton ib_back;
    private CardView cv_1month, cv_6month, cv_12month;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);


        init();

        event();


    }

    private void event() {
        ib_back.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        });
        cv_1month.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("subscription", Config.remove_ads_one_month);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        });
        cv_6month.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("subscription", Config.remove_ads_six_month);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        });
        cv_12month.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("subscription", Config.remove_ads_one_year);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        });
    }

    private void init() {
        ib_back = findViewById(R.id.ib_back);
        cv_1month = findViewById(R.id.cv_1month);
        cv_6month = findViewById(R.id.cv_6month);
        cv_12month = findViewById(R.id.cv_12month);
    }
}
