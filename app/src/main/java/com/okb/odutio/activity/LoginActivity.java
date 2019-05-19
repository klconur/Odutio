package com.okb.odutio.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.okb.odutio.R;
import com.okb.odutio.controller.FacebookLoginController;
import com.okb.odutio.controller.GoogleLoginController;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int    LOGIN               = 999;
    private static final String TAG                 = "LgnAct";

    private GoogleApiClient googleApiClient;
    private FacebookLoginController fbController;
    private GoogleLoginController googleController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createGoogleApi();
        fbController             = new FacebookLoginController(this);
        googleController         = new GoogleLoginController(this, googleApiClient);
    }

    private void createGoogleApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void onFacebookClick(View v) {
        fbController.login();
    }

    public void onGoogleClick(View v) {
        googleController.login();
    }

    private void onLoginSuccess() {
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbController.onActivityResult(requestCode, resultCode, data);
        googleController.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN && resultCode == Activity.RESULT_OK) {
            onLoginSuccess();
        }
    }
}
