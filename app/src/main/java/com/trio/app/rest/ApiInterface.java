package com.trio.app.rest;


import com.trio.app.models.AchievedModel;
import com.trio.app.models.DistributorStockModel;
import com.trio.app.models.DistributorsModel;
import com.trio.app.models.InvoiceDetailModel;
import com.trio.app.models.InvoiceModel;
import com.trio.app.models.LoginModel;
import com.trio.app.models.RouteModel;
import com.trio.app.models.ShopCreated;
import com.trio.app.models.ShopModel;
import com.trio.app.models.TargetModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by User on 08-Jan-18.
 */


public interface ApiInterface {

    @GET
    Call<LoginModel> loginUser(@Url String url);

    @GET
    Call<TargetModel> getUserTarget(@Url String url);

    @GET
    Call<AchievedModel> getUserAchieved(@Url String url);

    @GET
    Call<List<DistributorsModel>> getDistributors(@Url String url);

    @GET
    Call<List<RouteModel>> getRoutes(@Url String url);

    @GET
    Call<List<ShopModel>> getShopsList(@Url String url);

    @GET
    Call<ShopCreated> createShop(@Url String url);

    @GET
    Call<ShopCreated> updateProfile(@Url String url);

    @GET
    Call<List<DistributorStockModel>> getDistributorStock(@Url String url);

    @GET
    Call<List<InvoiceModel>> getInvoiceList(@Url String url);

    @GET
    Call<List<InvoiceDetailModel>> getInvoiceDetails(@Url String url);
}


