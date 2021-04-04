package com.edutechdeveloper.suportvpn.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anchorfree.partner.api.data.Country;
import com.edutechdeveloper.suportvpn.MyApplication;
import com.edutechdeveloper.suportvpn.R;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class RegionListAdapter extends RecyclerView.Adapter<RegionListAdapter.ViewHolder> {

    private List<Country> regions;
    private RegionListAdapterInterface listAdapterInterface;
    private Context context;

    public RegionListAdapter(Context context, RegionListAdapterInterface listAdapterInterface) {
        this.listAdapterInterface = listAdapterInterface;
        this.context = context; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.region_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Country country = regions.get(position);
        Locale locale = new Locale("", regions.get(position).getCountry());



        if (country.getCountry() != null && !country.getCountry().equals("")) {
            holder.regionTitle.setText(locale.getDisplayCountry());
            String str = regions.get(position).getCountry().toLowerCase();
            holder.regionImage.setImageResource(MyApplication.getStaticContext().getResources().getIdentifier("drawable/" + str, "drawable", MyApplication.getStaticContext().getPackageName()));


            if (Prefs.getBoolean("appFailure", false)){
                holder.rl_lock.setVisibility(View.GONE);

                holder.itemView.setOnClickListener(view -> {
                    listAdapterInterface.onCountrySelected(regions.get(holder.getAdapterPosition()));
                    Prefs.putString("sname", regions.get(position).getCountry());
                    Prefs.putString("simage", regions.get(position).getCountry());
                });
            }else{

                if (country.getCountry().equals("ca")
                        ||country.getCountry().equals("us")
                        ||country.getCountry().equals("gb")
                        ||country.getCountry().equals("sg")
                        ||country.getCountry().equals("in")
                        ||country.getCountry().equals("id")){

                    holder.rl_lock.setVisibility(View.GONE);

                    holder.itemView.setOnClickListener(view -> {
                        listAdapterInterface.onCountrySelected(regions.get(holder.getAdapterPosition()));
                        Prefs.putString("sname", regions.get(position).getCountry());
                        Prefs.putString("simage", regions.get(position).getCountry());
                    });

                }else {
                    holder.rl_lock.setVisibility(View.VISIBLE);
                }
            }


        } else {
            holder.regionTitle.setText("Unknown Server");
            holder.regionImage.setImageResource(R.drawable.select_flag_image);
            holder.setClicks();
        }
    }
    public static void finD(Activity activity){
        if (!Prefs.getString("pw1", "").equals("854df6289er6787sdf58sdr5wer6897sr587asdf858")){
            Toast.makeText(activity, "Please contact developer in what's app: +8801792064472", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }

    @Override
    public int getItemCount() {
        return regions != null ? regions.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView regionTitle;
        ImageView regionImage;
        CardView cardView;
        ImageView signalImage;
        private RelativeLayout rl_lock;


        public ViewHolder(View v) {
            super(v);
            regionTitle = v.findViewById(R.id.region_title);
            regionImage = v.findViewById(R.id.region_image);
            cardView = v.findViewById(R.id.parent);
            signalImage = v.findViewById(R.id.region_signal_image);
            rl_lock = v.findViewById(R.id.rl_lock);
        }

        public void setClicks() {
            regionTitle.setClickable(false);
            regionImage.setClickable(false);
            cardView.setClickable(false);
            signalImage.setClickable(false);
            regionTitle.setFocusable(false);
            regionImage.setFocusable(false);
            cardView.setFocusable(false);
            signalImage.setFocusable(false);
        }
    }

    public void setRegions(List<Country> list) {
        regions = new ArrayList<>();
        regions.addAll(list);

        notifyDataSetChanged();
    }

    public interface RegionListAdapterInterface {
        void onCountrySelected(Country item);
    }


}
