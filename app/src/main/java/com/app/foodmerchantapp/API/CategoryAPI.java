package com.app.foodmerchantapp.API;

import com.app.foodmerchantapp.Config.Constants;
import com.app.foodmerchantapp.Model.Category;
import com.app.foodmerchantapp.Model.Food;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    CategoryAPI categoryAPI = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CategoryAPI.class);

    @GET("/api/v1/merchant/categories")
    Call<List<Category>> getAllCategories();

}