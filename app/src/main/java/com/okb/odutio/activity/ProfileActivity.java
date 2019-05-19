package com.okb.odutio.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.okb.odutio.R;
import com.okb.odutio.UserPreference;
import com.okb.odutio.service.Client;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView    emailTextView;
    private TextView    incomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void onSendFeedbackClick(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","info@odutio.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Odutio android app feedback");
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_feedback)));
    }

    private void findViews() {
        emailTextView  = (TextView)    findViewById(R.id.email);
        incomeTextView = (TextView)    findViewById(R.id.income);
        progressBar    = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void init() {
        emailTextView.setText(UserPreference.getUsername());
        Client.getTotalIncome(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    String text = String.format(Locale.getDefault(), "$%.1f", response.body());
                    incomeTextView.setText(text);
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
