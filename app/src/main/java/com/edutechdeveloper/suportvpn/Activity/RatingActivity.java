package com.edutechdeveloper.suportvpn.Activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.edutechdeveloper.suportvpn.R;
import com.pixplicity.easyprefs.library.Prefs;

public class RatingActivity extends AppCompatActivity {

    private ImageButton ib_close;
    private Button btn_yes_rate, btn_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        init();
        event();
    }

    private void event() {
        ib_close.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        });
        btn_yes_rate.setOnClickListener(view -> {

            Prefs.putBoolean("isRate", true);

            try {
                Intent rateIntent = rateIntentForUrl("market://details");
                startActivity(rateIntent);
            }
            catch (ActivityNotFoundException e) {
                Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
                startActivity(rateIntent);
            }
        });
        btn_no.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        });

    }

    private void init() {
        ib_close = findViewById(R.id.ib_close);
        btn_yes_rate = findViewById(R.id.btn_yes_rate);
        btn_no = findViewById(R.id.btn_no);

    }
    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }
}
