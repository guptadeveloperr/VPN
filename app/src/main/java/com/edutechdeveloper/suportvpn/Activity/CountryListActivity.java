package com.edutechdeveloper.suportvpn.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anchorfree.partner.api.data.Country;
import com.edutechdeveloper.suportvpn.Adapter.RegionListAdapter;
import com.edutechdeveloper.suportvpn.Helper.Config;
import com.edutechdeveloper.suportvpn.R;

public class CountryListActivity extends AppCompatActivity implements RegionListAdapter.RegionListAdapterInterface{

    RecyclerView regionsRecyclerView;

    ProgressBar regionsProgressBar;

    private RegionListAdapter regionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);


        regionsProgressBar = findViewById(R.id.regions_progress);
        regionsRecyclerView =findViewById(R.id.regions_recycler_view);

        regionsRecyclerView.setHasFixedSize(true);
        regionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        regionAdapter = new RegionListAdapter(this, this);
        regionsRecyclerView.setAdapter(regionAdapter);

        loadServers();


    }

    private void loadServers() {
        hideProgress();
        regionAdapter.setRegions(Config.regions);
    }

    private void hideProgress() {
        regionsProgressBar.setVisibility(View.GONE);
        regionsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgress() {
        regionsProgressBar.setVisibility(View.VISIBLE);
        regionsRecyclerView.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onCountrySelected(Country item) {
        if (item != null){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("country",item.getCountry());
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    }

}
