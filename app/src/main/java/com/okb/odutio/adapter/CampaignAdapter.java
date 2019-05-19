package com.okb.odutio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.okb.odutio.R;
import com.okb.odutio.model.CampaignModel;

public class CampaignAdapter extends ArrayAdapter<CampaignModel> {

    public CampaignAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CampaignAdapter.ViewHolder holder;

        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_campaign, null);
            holder = new CampaignAdapter.ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (CampaignAdapter.ViewHolder) v.getTag();
        }
        CampaignModel item = getItem(position);
        if (item != null) {
            setCampaignView(item, holder);
        }
        return v;
    }

    private void setCampaignView(CampaignModel item, CampaignAdapter.ViewHolder holder) {
        Glide.with(getContext())
                .load(item.getAppIconUrl())
                .centerCrop()
                .crossFade()
                .dontAnimate()
                .into(holder.appIcon);
        holder.appName.setText(item.getAppName());
        holder.budget.setText(item.getCurrency() + item.getBudget() + " " + getContext().getString(R.string.per_installation));
        holder.description.setText(item.getAppDescription());
        if (item.getType().equals("appstore")) {
            holder.appTypeIcon.setImageResource(R.drawable.app_store);
        } else if (item.getType().equals("googleplay")) {
            holder.appTypeIcon.setImageResource(R.drawable.google_play);
        }
    }

    static class ViewHolder {
        ImageView appIcon;
        ImageView appTypeIcon;
        TextView  appName;
        TextView  budget;
        TextView  description;

        public ViewHolder(View view) {
            appIcon     = (ImageView) view.findViewById(R.id.app_icon);
            appTypeIcon = (ImageView) view.findViewById(R.id.app_type_icon);
            appName     = (TextView) view.findViewById(R.id.app_name);
            budget      = (TextView) view.findViewById(R.id.budget);
            description = (TextView) view.findViewById(R.id.app_description);
        }
    }
}
