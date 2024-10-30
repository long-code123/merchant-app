package com.app.foodmerchantapp.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.foodmerchantapp.API.FoodAPI;
import com.app.foodmerchantapp.API.StoreAPI;
import com.app.foodmerchantapp.Adapter.FoodAdapter;
import com.app.foodmerchantapp.Model.Food;
import com.app.foodmerchantapp.R;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodListActivity extends AppCompatActivity {

    private RecyclerView foodRecyclerView;
    private FoodAdapter foodAdapter;
    private StoreAPI storeAPI;
    private ImageView btnBack, btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        // Khởi tạo RecyclerView
        foodRecyclerView = findViewById(R.id.viewFood);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int storeId = getIntent().getIntExtra("storeId", -1);
        int categoryId = getIntent().getIntExtra("categoryId", -1);
        // Khởi tạo API
        storeAPI = StoreAPI.storeAPI;
        btnCreate = findViewById(R.id.btnCreate);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Quay lại Fragment Home
            onBackPressed();
        });

        // Thiết lập sự kiện nhấn nút Create
        btnCreate.setOnClickListener(v -> showCreateFoodDialog(storeId, categoryId));

        if (storeId != -1 && categoryId != -1) {
            loadFoodListByCategory(storeId, categoryId);
        } else {
            Toast.makeText(this, "Invalid store or category ID.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFoodListByCategory(int storeId, int categoryId) {
        Call<List<Food>> call = storeAPI.getFoodsByStoreAndCategory(storeId, categoryId);
        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Food> foodList = response.body();
                    // Khởi tạo Adapter và thiết lập cho RecyclerView
                    foodAdapter = new FoodAdapter(FoodListActivity.this, foodList);
                    foodRecyclerView.setAdapter(foodAdapter);
                } else {
                    Toast.makeText(FoodListActivity.this, "No data food", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(FoodListActivity.this, "Failed call API", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showCreateFoodDialog(int storeId, int categoryId) {
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create_food, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);

        // Find views in dialog
        EditText editFoodName = dialogView.findViewById(R.id.editFoodName);
        EditText editFoodImage = dialogView.findViewById(R.id.editFoodImage);
        EditText editDescription = dialogView.findViewById(R.id.editDescription);
        EditText editPrice = dialogView.findViewById(R.id.editPrice);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        btnSave.setOnClickListener(v -> {
            String foodName = editFoodName.getText().toString().trim();
            String foodImage = editFoodImage.getText().toString().trim();
            String description = editDescription.getText().toString().trim();
            String priceStr = editPrice.getText().toString().trim();

            // Validate input fields
            if (foodName.isEmpty() || foodImage.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(FoodListActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(FoodListActivity.this, "Invalid price format", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new Food object (exclude timestamps)
            Food food = new Food();
            food.setFoodName(foodName);
            food.setFoodImage(foodImage);
            food.setDescription(description);
            food.setPrice(price);
            food.setCategoryId(categoryId);
            food.setStoreId(storeId);

            // Call API to create food
            createFood(food, dialog);
        });
    }

    private void createFood(Food food, AlertDialog dialog) {
        // Call the API to create food
        FoodAPI.foodAPI.createFood(food).enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(FoodListActivity.this, "Food created successfully!", Toast.LENGTH_SHORT).show();
                    // Refresh the food list or update the adapter if necessary
                    loadFoodListByCategory(food.getStoreId(), food.getCategoryId());
                    dialog.dismiss();
                } else {
                    Toast.makeText(FoodListActivity.this, "Failed to create food", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(FoodListActivity.this, "Failed to call API", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
