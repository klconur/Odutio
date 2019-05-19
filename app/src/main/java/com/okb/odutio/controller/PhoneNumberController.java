package com.okb.odutio.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.okb.odutio.R;
import com.okb.odutio.activity.ContactActivity;
import com.okb.odutio.model.CampaignModel;

/**
 * Created by onurkilic on 09/05/2017.
 */

public class PhoneNumberController {

    private Activity      activity;
    private CampaignModel item;

    public PhoneNumberController(Activity activity, CampaignModel item) {
        this.activity = activity;
        this.item     = item;
    }

    public void getNumber() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        final AppCompatEditText input = new AppCompatEditText(activity);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setHint(R.string.phone_number);
        FrameLayout container = new FrameLayout(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(dpToPx(25));
        params.setMarginEnd(dpToPx(25));
        input.setSingleLine();
        input.setLayoutParams(params);
        container.addView(input);
        alert.setView(container);
        alert.setMessage(R.string.phone_number_message);
        alert.setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(input.getText().toString())) {
                    ShareController controller = new ShareController(activity, item);
                    controller.share(input.getText().toString());
                } else {
                    Toast.makeText(activity, R.string.phone_number_warning, Toast.LENGTH_SHORT).show();
                }
            }

        });
        alert.setNegativeButton(R.string.contacts, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivityForResult(new Intent(activity, ContactActivity.class), 0);
            }

        });
        alert.show();
    }

    private int dpToPx(int dp) {
        return (int) (dp * activity.getResources().getSystem().getDisplayMetrics().density);
    }
}
