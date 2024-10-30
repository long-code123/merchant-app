package com.app.foodmerchantapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.foodmerchantapp.API.OrderAPI;
import com.app.foodmerchantapp.Model.Order;
import com.app.foodmerchantapp.Model.OrderItem;
import com.app.foodmerchantapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.textOrderId.setText(String.valueOf(order.getOrderId()));

        String createdAt = order.getCreatedAt();

        // Định dạng cho việc phân tích chuỗi
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

        try {
            // Chuyển đổi chuỗi thành Date
            Date date = inputFormat.parse(createdAt);

            // Định dạng để lấy ngày và giờ
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

            // Lấy ngày và giờ
            String dateString = dateFormat.format(date);
            String timeString = timeFormat.format(date);

            // Gán giá trị cho TextView
            holder.textDate.setText(dateString); // Gán giá trị ngày
            holder.textTime.setText(timeString); // Gán giá trị giờ

        } catch (ParseException e) {
            e.printStackTrace();
            holder.textDate.setText("N/A");
            holder.textTime.setText("N/A");
        }

        // Tính tổng tiền của đơn hàng
        double total = 0;
        for (OrderItem item : order.getItems()) {
            total += item.getQuantity() * item.getFood().getPrice();
        }
        holder.textTotal.setText(String.format("$%.2f", total));

        holder.textStatus.setText(order.getStatus());

        holder.btnDetail.setOnClickListener(v -> showOrderDetailDialog(order));

    }

    // Phương thức cập nhật danh sách đơn hàng
    public void updateOrders(List<Order> newOrders) {
        this.orderList.clear();  // Xóa danh sách cũ
        this.orderList.addAll(newOrders);  // Thêm danh sách mới
        notifyDataSetChanged();  // Thông báo cho RecyclerView rằng dữ liệu đã thay đổi
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textOrderId, textTotal, textStatus, textDate, textTime;
        Button btnDetail;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrderId = itemView.findViewById(R.id.textOrderId);
            textTotal = itemView.findViewById(R.id.textTotal);
            textStatus = itemView.findViewById(R.id.textStatus);
            textDate = itemView.findViewById(R.id.textDate);
            textTime = itemView.findViewById(R.id.textTime);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }

    private void showOrderDetailDialog(Order order) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_layout);

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerViewOrderDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        List<OrderItem> orderItems = order.getItems();
        OrderDetailAdapter adapter = new OrderDetailAdapter(orderItems);
        recyclerView.setAdapter(adapter);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnAcceptOrder = dialog.findViewById(R.id.btnAcceptOrder);
        Button btnCancelOrder = dialog.findViewById(R.id.btnCancelOrder);
        TextView textOrderIdDetail = dialog.findViewById(R.id.textOrderIdDetail);

        textOrderIdDetail.setText("Order ID: " + order.getOrderId());

        if ("pending".equalsIgnoreCase(order.getStatus())) {
            btnAcceptOrder.setVisibility(View.VISIBLE);
            btnCancelOrder.setVisibility(View.VISIBLE);

            // Accept Order button
            btnAcceptOrder.setOnClickListener(v -> {
                OrderAPI orderAPI = OrderAPI.orderAPI;
                Call<Order> call = orderAPI.acceptOrder(String.valueOf(order.getOrderId()), order);
                call.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Order Accepted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to accept order", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            });

            // Cancel Order button
            btnCancelOrder.setOnClickListener(v -> {
                OrderAPI orderAPI = OrderAPI.orderAPI;
                Call<Order> call = orderAPI.cancelOrder(String.valueOf(order.getOrderId()), order);
                call.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Order Canceled", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to cancel order", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            });
        } else {
            btnAcceptOrder.setVisibility(View.GONE);
            btnCancelOrder.setVisibility(View.GONE);
        }

        // Center the dialog with a fixed width and height
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85); // 85% width
            params.height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.85); // 85% height
            window.setAttributes(params);
        }

        dialog.show();
    }
}
