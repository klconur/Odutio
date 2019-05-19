package com.okb.odutio.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.okb.odutio.R;
import com.okb.odutio.UserPreference;
import com.okb.odutio.model.CampaignModel;
import com.okb.odutio.model.ResultModel;
import com.okb.odutio.service.Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShareController
{
    private static final int TYPE_SHARED = 0;

    private ProgressDialog dialog;

    private Activity      activity;
    private CampaignModel item;
    private String        phoneNumber;

    public ShareController(Activity activity, CampaignModel item) {
        this.activity = activity;
        this.item     = item;
    }

    public void share(String number) {
        phoneNumber = number.replaceAll("[\\D]", "");
        phoneNumber = phoneNumber.replaceFirst("^0+(?!$)", "");
        dialog = ProgressDialog.show(activity, null, activity.getString(R.string.please_wait), true);
        Client.addTransaction(item.getCampaignId(), TYPE_SHARED, phoneNumber, callback);
    }

    private void openWhatsApp(String number, String token) {
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, getUrl(token));
            sendIntent.putExtra("jid", number + "@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            activity.startActivity(sendIntent);
        } catch(Exception e) {
            Toast.makeText(activity, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getUrl(String token) {
        return "https://www.odutio.com/track.php?token=" + token;
    }

    private void showAlert() {
        new AlertDialog.Builder(activity)
                .setMessage(R.string.installed_warning)
                .setNegativeButton(R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private Callback<ResultModel> callback = new Callback<ResultModel>() {

        @Override
        public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
            dialog.dismiss();
            dialog = null;
            if (response.isSuccessful()) {
                if (response.body().isSuccess()) {
                    openWhatsApp(phoneNumber, response.body().getTag());
                } else {
                    showAlert();
                }
            }
        }

        @Override
        public void onFailure(Call<ResultModel> call, Throwable t) {
            dialog.dismiss();
            dialog = null;
            Toast.makeText(activity, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}
