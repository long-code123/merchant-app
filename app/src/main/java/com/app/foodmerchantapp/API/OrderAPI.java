package com.app.foodmerchantapp.API;

import com.app.foodmerchantapp.Config.Constants;
import com.app.foodmerchantapp.Model.Order;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    OrderAPI orderAPI = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OrderAPI.class);

    // API PUT để chấp nhận đơn hàng
    @PUT("/api/v1/merchant/orders/{id}/accept")
    Call<Order> acceptOrder(@Path("id") String orderId, @Body Order order);

    // API PUT để hủy đơn hàng
    @PUT("/api/v1/merchant/orders/{id}/cancel")
    Call<Order> cancelOrder(@Path("id") String orderId, @Body Order order);
}
