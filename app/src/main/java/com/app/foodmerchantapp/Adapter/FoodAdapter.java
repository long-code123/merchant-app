package com.app.foodmerchantapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.foodmerchantapp.API.FoodAPI;
import com.app.foodmerchantapp.Model.Food;
import com.app.foodmerchantapp.R;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private Context context;
    private List<Food> foodList;

    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);

        // Bind the data to each TextView
        holder.foodName.setText(food.getFoodName());
        holder.foodDescription.setText(food.getDescription());
        holder.foodPrice.setText(String.format("$%.2f", food.getPrice()));

        // Load the image using Glide (assuming URL or drawable resource)
        Glide.with(context)
                .load(food.getFoodImage()) // Replace with food.getImageUrl() or similar if using URLs
                .placeholder(R.drawable.ic_launcher_background) // Placeholder image if needed
                .into(holder.foodImage);

        // Set click listener for delete button
        holder.btnDelete.setOnClickListener(view -> {
            showDeleteConfirmationDialog(food.getFoodId(), position);
        });

        // Set click listener for update button
        holder.btnUpdate.setOnClickListener(view -> {
            showUpdateFoodDialog(food, position);
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodDescription, foodPrice;
        ImageView foodImage, btnDelete, btnUpdate;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodDescription = itemView.findViewById(R.id.foodDescription);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodImage = itemView.findViewById(R.id.foodImage);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);

        }
    }
    private void showDeleteConfirmationDialog(int foodId, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure delete this food?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteFood(foodId, position);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteFood(int foodId, int position) {
        FoodAPI.foodAPI.deleteFood(foodId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa món ăn khỏi danh sách và thông báo cho adapter
                    foodList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, foodList.size());
                    Toast.makeText(context, "Delete successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateFoodDialog(Food food, int position) {
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_food, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(dialogView);

        // Find views in dialog
        EditText editFoodName = dialogView.findViewById(R.id.editFoodName);
        EditText editDescription = dialogView.findViewById(R.id.editDescription);
        EditText editPrice = dialogView.findViewById(R.id.editPrice);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        // Set current values in the dialog
        editFoodName.setText(food.getFoodName());
        editDescription.setText(food.getDescription());
        editPrice.setText(String.valueOf(food.getPrice()));

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        btnSave.setOnClickListener(v -> {
            // Get updated values
            String updatedName = editFoodName.getText().toString();
            String updatedDescription = editDescription.getText().toString();
            double updatedPrice = Double.parseDouble(editPrice.getText().toString());

            // Update food object
            food.setFoodName(updatedName);
            food.setDescription(updatedDescription);
            food.setPrice(updatedPrice);

            // Call API to update food
            updateFood(food, position, dialog);
        });
    }

    private void updateFood(Food food, int position, AlertDialog dialog) {
        FoodAPI.foodAPI.updateFood(food.getFoodId(), food).enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful()) {
                    // Update the food item in the list and notify the adapter
                    foodList.set(position, response.body()); // Replace with the updated food from response
                    notifyItemChanged(position);
                    Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show();
                    dialog.dismiss(); // Dismiss the dialog
                } else {
                    Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
