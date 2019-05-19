package com.okb.odutio.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.okb.odutio.R;
import com.okb.odutio.model.SharedItemModel;
import com.okb.odutio.service.Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharedItemsAdapter extends ArrayAdapter<SharedItemModel> {

    public SharedItemsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        SharedItemsAdapter.ViewHolder holder;

        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_shared_item, null);
            holder = new SharedItemsAdapter.ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (SharedItemsAdapter.ViewHolder) v.getTag();
        }
        SharedItemModel item = getItem(position);
        if (item != null) {
            setCampaignView(item, holder);
        }
        return v;
    }

    private void setCampaignView(final SharedItemModel item, final SharedItemsAdapter.ViewHolder holder) {
        holder.appName.setText(item.getAppName());
        holder.phoneNumber.setText(item.getNumber());
        if (item.getAppType().equals("googleplay")) {
            holder.appType.setImageResource(R.drawable.google_play);
        } else if (item.getAppType().equals("appstore")) {
            holder.appType.setImageResource(R.drawable.app_store);
        }
        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.approveButton.setEnabled(false);
                Client.approveItem(item.getToken(), new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            holder.approveButton.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        holder.approveButton.setEnabled(true);
                    }
                });
            }
        });
    }

    static class ViewHolder {
        AppCompatButton approveButton;
        ImageView appType;
        TextView  appName;
        TextView  phoneNumber;

        public ViewHolder(View view) {
            approveButton = (AppCompatButton) view.findViewById(R.id.approve_button);
            appType     = (ImageView) view.findViewById(R.id.app_type_icon);
            appName     = (TextView) view.findViewById(R.id.app_name);
            phoneNumber = (TextView) view.findViewById(R.id.phone_number);
        }
    }
}
