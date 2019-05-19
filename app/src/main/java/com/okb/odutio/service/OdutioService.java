package com.okb.odutio.service;

import com.okb.odutio.model.CampaignModel;
import com.okb.odutio.model.ResultModel;
import com.okb.odutio.model.SharedItemModel;
import com.okb.odutio.model.TokenModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by onurkilic on 08/05/2017.
 */

public interface OdutioService {

    @FormUrlEncoded
    @POST("register.php")
    Call<TokenModel> register(@Field("email") String email,
                                  @Field("given_name") String givenName,
                                  @Field("family_name") String familyName,
                                  @Field("provider") String provider,
                                  @Field("os_type") String osType,
                                  @Field("country_iso") String countryIso);

    @GET("campaign.php")
    Call<List<CampaignModel>> getCampaigns();

    @FormUrlEncoded
    @POST("transaction.php")
    Call<ResultModel> addTransaction(@Field("campaign_id") int campaignId, @Field("type") int type, @Field("number") String number);

    @GET("income.php")
    Call<Double> getTotalIncome();

    @GET("shared.php")
    Call<List<SharedItemModel>> getSharedItems();

    @FormUrlEncoded
    @POST("approve.php")
    Call<Void> approve(@Field("token") String token);
}
