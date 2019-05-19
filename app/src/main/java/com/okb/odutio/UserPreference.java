package com.okb.odutio;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.okb.odutio.model.TokenModel;

/**
 * Created by onurkilic on 08/05/2017.
 */

public class UserPreference {

    private static final String USERNAME         = "_username";
    private static final String FIRSTNAME        = "_first_name";
    private static final String LASTNAME         = "_last_name";
    private static final String ACCESS_TOKEN     = "_access_token";
    private static final String REFERENCE_CODE   = "_reference_code";

    public static void setUsername(String username, String firstname, String lastname) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USERNAME,  username);
        editor.putString(FIRSTNAME, firstname);
        editor.putString(LASTNAME,  lastname);
        editor.apply();
    }

    public static String getReferenceCode() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return prefs.getString(REFERENCE_CODE, null);
    }

    public static String getUsername() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return prefs.getString(USERNAME, null);
    }

    public static boolean isLoggedIn() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return prefs.contains(ACCESS_TOKEN);
    }

    public static String getAccessToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return prefs.getString(ACCESS_TOKEN, null);
    }

    public static void storeToken(TokenModel token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ACCESS_TOKEN,   token.getToken());
        editor.putString(REFERENCE_CODE, token.getReferenceCode());
        editor.commit();
    }

    private static Context getContext() {
        return OdutioApplication.getAppContext();
    }
}
