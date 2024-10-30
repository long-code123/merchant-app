package com.app.foodmerchantapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.foodmerchantapp.Model.OrderItem;
import com.app.foodmerchantapp.R;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    private List<OrderItem> orderItems;

    public OrderDetailAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);
        holder.textFoodName.setText(item.getFood().getFoodName());
        holder.textPrice.setText(String.format("$%.2f", item.getFood().getPrice()));
        holder.textQuantity.setText(String.valueOf(item.getQuantity()));

        // Tính tổng tiền
        double total = item.getFood().getPrice() * item.getQuantity();
        holder.textTotal.setText(String.format("$%.2f", total));
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textFoodName, textPrice, textQuantity, textTotal; // Thêm biến cho tổng tiền

        public ViewHolder(View itemView) {
            super(itemView);
            textFoodName = itemView.findViewById(R.id.textFoodName);
            textPrice = itemView.findViewById(R.id.textPrice);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textTotal = itemView.findViewById(R.id.textTotal); // Kết nối với TextView tổng tiền
        }
    }
}
