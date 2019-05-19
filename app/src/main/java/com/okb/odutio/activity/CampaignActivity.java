package com.okb.odutio.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.okb.odutio.R;
import com.okb.odutio.controller.PhoneNumberController;
import com.okb.odutio.controller.ShareController;
import com.okb.odutio.model.CampaignModel;

public class CampaignActivity extends AppCompatActivity {

    private CampaignModel item;

    private ImageView appIcon;
    private ImageView appTypeIcon;
    private TextView  appName;
    private TextView  budget;
    private TextView  description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        item = (CampaignModel) getIntent().getSerializableExtra("campaign");
        setTitle(item.getAppName());
        findViews();
        init();
    }

    private void findViews() {
        appIcon     = (ImageView) findViewById(R.id.app_icon);
        appTypeIcon = (ImageView) findViewById(R.id.app_type_icon);
        appName     = (TextView)  findViewById(R.id.app_name);
        budget      = (TextView)  findViewById(R.id.budget);
        description = (TextView)  findViewById(R.id.app_description);
    }

    private void init() {
        Glide.with(this)
                .load(item.getAppIconUrl())
                .centerCrop()
                .crossFade()
                .dontAnimate()
                .into(appIcon);
        appName.setText(item.getAppName());
        budget.setText(item.getCurrency() + item.getBudget() + " " + getString(R.string.per_installation));
        description.setText(item.getAppDescription());
        if (item.getType().equals("appstore")) {
            appTypeIcon.setImageResource(R.drawable.app_store);
        } else if (item.getType().equals("googleplay")) {
            appTypeIcon.setImageResource(R.drawable.google_play);
        }
    }

    public void onShareClicked(View v) {
        PhoneNumberController controller = new PhoneNumberController(this, item);
        controller.getNumber();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String number = data.getStringExtra("phone_number");
            ShareController controller = new ShareController(this, item);
            controller.share(number);
        }
    }
}
