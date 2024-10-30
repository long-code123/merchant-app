package com.app.foodmerchantapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.foodmerchantapp.Model.Category;
import com.app.foodmerchantapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private Context context;
    private OnCategoryClickListener listener;  // Thêm listener

    public CategoryAdapter(Context context, List<Category> categories, OnCategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;  // Khởi tạo listener
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);

        // Set category name
        holder.textCategoryName.setText(category.getCategoryName());

        // Load category image using Picasso
        Picasso.get()
                .load(category.getCategoryImage())
                .into(holder.imageCategory);

        // Xử lý sự kiện nhấn mục danh mục
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(category.getCategoryId()); // Gọi listener chỉ với categoryId
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textCategoryName;
        ImageView imageCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategoryName = itemView.findViewById(R.id.textCategoryName);
            imageCategory = itemView.findViewById(R.id.imageCategory);
        }
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(int categoryId); // Chỉ cần categoryId
    }
}