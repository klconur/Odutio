package com.okb.odutio.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.okb.odutio.model.CampaignModel;
import com.okb.odutio.model.ResultModel;
import com.okb.odutio.model.SharedItemModel;
import com.okb.odutio.model.TokenModel;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by onurkilic on 08/05/2017.
 */

public class Client {

    private static final String BASE_URL        = "https://www.odutio.com/api/";

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    private static final OkHttpClient client = new OkHttpClient.Builder()
//            .authenticator(new TokenAuthenticator())
            .addInterceptor(new HeaderInterceptor(true))
            .addInterceptor(loggingInterceptor)
            .build();

    static {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public static void getCampaigns(Callback<List<CampaignModel>> callback) {
        OdutioService service = getService();
        Call<List<CampaignModel>> call = service.getCampaigns();
        call.enqueue(callback);
    }

    public static void addTransaction(int campaignId, int type, String number, Callback<ResultModel> callback) {
        OdutioService service = getService();
        Call<ResultModel> call = service.addTransaction(campaignId, type, number);
        call.enqueue(callback);
    }

    public static void getTotalIncome(Callback<Double> callback) {
        OdutioService service = getService();
        Call<Double> call = service.getTotalIncome();
        call.enqueue(callback);
    }

    public static void getSharedItems(Callback<List<SharedItemModel>> callback) {
        OdutioService service = getService();
        Call<List<SharedItemModel>> call = service.getSharedItems();
        call.enqueue(callback);
    }

    public static void approveItem(String token, Callback<Void> callback) {
        OdutioService service = getService();
        Call<Void> call = service.approve(token);
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
