package com.app.foodmerchantapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.foodmerchantapp.API.StoreAPI;
import com.app.foodmerchantapp.Adapter.OrderAdapter;
import com.app.foodmerchantapp.Model.Order;
import com.app.foodmerchantapp.Model.Store;
import com.app.foodmerchantapp.R;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {

    private RecyclerView orderView;
    private OrderAdapter orderAdapter;
    private ImageView btnRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        orderView = view.findViewById(R.id.orderView);
        btnRefresh = view.findViewById(R.id.btnRefresh); // Kết nối với ImageView refresh

        // Thiết lập LayoutManager cho RecyclerView
        orderView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderView.setHasFixedSize(true);

        loadOrders(); // Gọi API để tải danh sách đơn hàng

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOrders();
            }
        });

        return view;
    }

    private void loadOrders() {
        // Lấy storeId từ SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MerchantAppPrefs", Context.MODE_PRIVATE);
        String storeJson = sharedPreferences.getString("store_info", null);

        if (storeJson != null) {
            Store store = new Gson().fromJson(storeJson, Store.class);
            int storeId = store.getStoreId();  // Lấy storeId từ Store

            // Gọi API để lấy danh sách đơn hàng
            StoreAPI.storeAPI.getOrdersByStore(storeId).enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Order> orders = response.body();
                        orderAdapter = new OrderAdapter(getContext(), orders);
                        orderView.setAdapter(orderAdapter);
                    } else {
                        Toast.makeText(getContext(), "Failed to load orders.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Xử lý nếu không có store thông tin lưu
            Toast.makeText(getContext(), "Store information not found", Toast.LENGTH_SHORT).show();
        }
    }
}
