package com.okb.odutio.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.okb.odutio.R;
import com.okb.odutio.UserPreference;
import com.okb.odutio.activity.MainActivity;
import com.okb.odutio.model.TokenModel;
import com.okb.odutio.service.AuthClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by onurkilic on 08/05/2017.
 */

public class GoogleLoginController implements Callback<TokenModel>
{
    private static final String TAG         = "GoogleLgn";
    private static final int    RC_SIGN_IN  = 998;

    private Activity activity;
    private GoogleApiClient googleApiClient;
    private ProgressDialog progress;

    public GoogleLoginController(Activity activity, GoogleApiClient googleApiClient) {
        this.activity        = activity;
        this.googleApiClient = googleApiClient;
    }

    public void login() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            progress = ProgressDialog.show(activity, null,
                    activity.getString(R.string.please_wait), true);
            GoogleSignInAccount acct = result.getSignInAccount();
//            if (acct.getPhotoUrl() != null) {
//                profilePicUrl = acct.getPhotoUrl().toString();
//            }
            UserPreference.setUsername(
                    acct.getEmail(),
                    acct.getGivenName(),
                    acct.getFamilyName());
            AuthClient.register(acct.getEmail(),
                            acct.getGivenName(),
                            acct.getFamilyName(),
                            "google",
                            getIso(),
                            this);
        } else {
            Toast.makeText(activity, result.getStatus().getStatusMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void onLoginSuccess() {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }

    private String getIso() {
        TelephonyManager teleMgr = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (teleMgr != null) {
            return teleMgr.getNetworkCountryIso();
        }
        return "";
    }

    @Override
    public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
        if (response.isSuccessful()) {
            UserPreference.storeToken(response.body());
            onLoginSuccess();
        }
    }

    @Override
    public void onFailure(Call<TokenModel> call, Throwable t) {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
        Toast.makeText(activity, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }
}
