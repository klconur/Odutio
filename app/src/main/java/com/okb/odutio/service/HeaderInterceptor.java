package com.okb.odutio.service;

import com.okb.odutio.UserPreference;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by onurkilic on 08/05/2017.
 */

public class HeaderInterceptor implements Interceptor {

    private boolean addAuthorization;

    public HeaderInterceptor(boolean addAuthorization) {
        this.addAuthorization = addAuthorization;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder();
        builder.header("Accept-Language", Locale.getDefault().getLanguage());
        if (addAuthorization) {
            builder.header("Authorization", UserPreference.getAccessToken());
        }
        builder.method(original.method(), original.body());
        Request request = builder.build();

        return chain.proceed(request);
    }
}
