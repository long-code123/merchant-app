package com.app.foodmerchantapp.API;

import com.app.foodmerchantapp.Config.Constants;
import com.app.foodmerchantapp.Model.Food;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FoodAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    FoodAPI foodAPI = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FoodAPI.class);

    @GET("/api/v1/merchant/foods")
    Call<List<Food>> getAllFood();

    @POST("/api/v1/merchant/foods")
    Call<Food> createFood(@Body Food food);

    @PUT("/api/v1/merchant/foods/{id}")
    Call<Food> updateFood(@Path("id") int id, @Body Food food);

    @DELETE("/api/v1/merchant/foods/{id}")
    Call<Void> deleteFood(@Path("id") int id);
}
