package com.app.foodmerchantapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.foodmerchantapp.API.CategoryAPI;
import com.app.foodmerchantapp.API.StoreAPI;
import com.app.foodmerchantapp.Activity.FoodListActivity;
import com.app.foodmerchantapp.Adapter.CategoryAdapter;
import com.app.foodmerchantapp.Adapter.ReviewStoreAdapter;
import com.app.foodmerchantapp.Model.Category;
import com.app.foodmerchantapp.Model.Food;
import com.app.foodmerchantapp.Model.ReviewStore;
import com.app.foodmerchantapp.Model.Store;
import com.app.foodmerchantapp.R;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final String PREFS_NAME = "MerchantAppPrefs";
    private static final String STORE_INFO_KEY = "store_info";

    private RecyclerView categoriesView;
    private RecyclerView storesView;
    private CategoryAdapter categoryAdapter;
    private ReviewStoreAdapter reviewStoreAdapter;
    private TextView textAverage, textTotal, tvStoreName;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initializeViews(view);
        setupCategoriesRecyclerView();
        setupStoresRecyclerView();

        loadStoreInfo();

        return view;
    }

    private void initializeViews(View view) {
        textTotal = view.findViewById(R.id.textTotal);
        textAverage = view.findViewById(R.id.textAverage);
        tvStoreName = view.findViewById(R.id.tvStoreName);
        categoriesView = view.findViewById(R.id.categoriesView);
        storesView = view.findViewById(R.id.storesView);
    }

    private void setupCategoriesRecyclerView() {
        categoriesView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        loadCategories();
    }

    private void setupStoresRecyclerView() {
        storesView.setLayoutManager(new LinearLayoutManager(getContext()));
        storesView.setHasFixedSize(true);
    }

    private void loadStoreInfo() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String storeJson = sharedPreferences.getString(STORE_INFO_KEY, null);

        if (storeJson != null) {
            Store store = new Gson().fromJson(storeJson, Store.class);
            tvStoreName.setText(store.getStoreName());
            loadStoreReviews(store.getStoreId());
        } else {
            Toast.makeText(getContext(), "Store information not found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCategories() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String storeJson = sharedPreferences.getString(STORE_INFO_KEY, null);

        if (storeJson == null) {
            Toast.makeText(getContext(), "Store information not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        Store store = new Gson().fromJson(storeJson, Store.class);
        int storeId = store.getStoreId();

        CategoryAPI.categoryAPI.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();
                    categoryAdapter = new CategoryAdapter(getContext(), categories, categoryId -> loadFoodByCategory(storeId, categoryId));
                    categoriesView.setAdapter(categoryAdapter);
                } else {
                    showError("Unable to load category list.");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                showError("Error: " + t.getMessage());
            }
        });
    }

    private void loadFoodByCategory(int storeId, int categoryId) {
        showFoodList(storeId, categoryId);
    }

    private void showFoodList(int storeId, int categoryId) {
        Intent intent = new Intent(getContext(), FoodListActivity.class);
        intent.putExtra("storeId", storeId); // Truyền storeId
        intent.putExtra("categoryId", categoryId); // Truyền categoryId

        startActivity(intent);
    }

    private void loadStoreReviews(int storeId) {
        StoreAPI.storeAPI.getReviewStoreByStore(storeId).enqueue(new Callback<List<ReviewStore>>() {
            @Override
            public void onResponse(Call<List<ReviewStore>> call, Response<List<ReviewStore>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReviewStore> reviews = response.body();
                    reviewStoreAdapter = new ReviewStoreAdapter(getContext(), reviews);
                    storesView.setAdapter(reviewStoreAdapter);

                    updateReviewStatistics(reviews);
                } else {
                    showError("Failed to load reviews.");
                }
            }

            @Override
            public void onFailure(Call<List<ReviewStore>> call, Throwable t) {
                showError("Error: " + t.getMessage());
            }
        });
    }

    private void updateReviewStatistics(List<ReviewStore> reviews) {
        int totalReviews = reviews.size();
        textTotal.setText("(" + "Total: " + totalReviews + ")");

        double totalRating = reviews.stream().mapToDouble(ReviewStore::getRating).sum();
        double averageRating = totalReviews > 0 ? totalRating / totalReviews : 0;
        textAverage.setText(String.format("%.1f", averageRating));
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
