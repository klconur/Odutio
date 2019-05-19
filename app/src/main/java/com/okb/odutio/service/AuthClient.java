package com.okb.odutio.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.okb.odutio.model.TokenModel;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by onurkilic on 08/05/2017.
 */

public class AuthClient {

    private static final String BASE_URL        = "https://www.odutio.com/api/";

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    private static final OkHttpClient client = new OkHttpClient.Builder()
//            .authenticator(new TokenAuthenticator())
//            .addInterceptor(new HeaderInterceptor(true))
            .addInterceptor(loggingInterceptor)
            .build();

    static {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public static void register(String email, String givenName, String familyName,
                                String provider, String iso, Callback<TokenModel> callback) {
        OdutioService service = getService();
        Call<TokenModel> call = service.register(email, givenName, familyName, provider, "android", iso);
        call.enqueue(callback);
    }

    private static OdutioService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(OdutioService.class);
    }
}
