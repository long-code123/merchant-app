package com.app.foodmerchantapp.API;

import com.app.foodmerchantapp.Config.Constants;
import com.app.foodmerchantapp.Model.ChartResponse;
import com.app.foodmerchantapp.Model.Food;
import com.app.foodmerchantapp.Model.LoginRequest;
import com.app.foodmerchantapp.Model.LoginResponse;
import com.app.foodmerchantapp.Model.Order;
import com.app.foodmerchantapp.Model.OrderData;
import com.app.foodmerchantapp.Model.ReviewStore;
import com.app.foodmerchantapp.Model.Store;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StoreAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    StoreAPI storeAPI = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(StoreAPI.class);

    @GET("/api/v1/merchant/stores/{id}/foods")
    Call<List<Food>> getFoodsByStore(@Path("id") int storeId);

    @GET("/api/v1/merchant/stores/{id}/reviews")
    Call<List<ReviewStore>> getReviewStoreByStore(@Path("id") int storeId);

    @GET("/api/v1/merchant/stores/{id}/income")
    Call<ChartResponse> calculateStoreIncome(@Path("id") int storeId);


    @GET("/api/v1/merchant/stores/{id}/orders")
    Call<List<Order>> getOrdersByStore(@Path("id") int storeId);

    @GET("/api/v1/merchant/stores/{storeId}/categories/{categoryId}/foods")
    Call<List<Food>> getFoodsByStoreAndCategory(@Path("storeId") int storeId, @Path("categoryId") int categoryId);

    @POST("/api/v1/merchant/stores/login")
    Call<LoginResponse> loginStore(@Body LoginRequest loginRequest);
}
