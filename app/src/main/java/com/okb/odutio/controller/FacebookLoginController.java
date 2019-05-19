package com.okb.odutio.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.okb.odutio.R;
import com.okb.odutio.UserPreference;
import com.okb.odutio.activity.MainActivity;
import com.okb.odutio.model.TokenModel;
import com.okb.odutio.service.AuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacebookLoginController implements FacebookCallback<LoginResult>, Callback<TokenModel>
{
    private Activity        activity;
    private CallbackManager callbackManager;
    private ProgressDialog  progress;

    public FacebookLoginController(Activity activity) {
        this.activity   = activity;
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    public void login() {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserInfo(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {
                            Toast.makeText(activity, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.v("LoginActivity", response.toString());
                            callSignIn(object);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location, picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void callSignIn(JSONObject object) {
        try {
//            if (object.has("picture")) {
//                profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
//            }
//            String location = null;
//            if (object.has("location")) {
//                location = object.getString("location");
//            }
//            String birthday = null;
//            if (object.has("birthday")) {
//                birthday = object.getString("birthday");
//            }
            UserPreference.setUsername(
                    object.getString("email"),
                    object.getString("first_name"),
                    object.getString("last_name"));
            AuthClient.register(object.getString("email"),
                            object.getString("first_name"),
                            object.getString("last_name"),
                            "facebook",
                            getIso(),
                            this);
        } catch (JSONException e) {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
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
    public void onSuccess(LoginResult loginResult) {
        progress = ProgressDialog.show(activity, null,
                activity.getString(R.string.please_wait), true);
        getUserInfo(loginResult);
    }

    @Override
    public void onCancel() {
        Log.v("LoginActivity", "cancel");
    }

    @Override
    public void onError(FacebookException exception) {
        Toast.makeText(activity, exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
