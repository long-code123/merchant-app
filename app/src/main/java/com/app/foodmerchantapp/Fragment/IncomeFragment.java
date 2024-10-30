package com.app.foodmerchantapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.foodmerchantapp.API.StoreAPI;
import com.app.foodmerchantapp.Model.ChartResponse;
import com.app.foodmerchantapp.Model.OrderData;
import com.app.foodmerchantapp.Model.Store;
import com.app.foodmerchantapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomeFragment extends Fragment {

    private BarChart barChart;
    private StoreAPI storeAPI;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        barChart = view.findViewById(R.id.incomeBarChart);
        storeAPI = StoreAPI.storeAPI; // Khởi tạo StoreAPI

        loadChartData();

        return view;
    }

    private void loadChartData() {
        // Lấy storeId từ SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MerchantAppPrefs", Context.MODE_PRIVATE);
        String storeJson = sharedPreferences.getString("store_info", null);

        if (storeJson != null) {
            Store store = new Gson().fromJson(storeJson, Store.class);
            int storeId = store.getStoreId(); // Lấy storeId từ Store

            // Gọi API để tính thu nhập của cửa hàng
            Call<ChartResponse> call = StoreAPI.storeAPI.calculateStoreIncome(storeId);
            call.enqueue(new Callback<ChartResponse>() {
                @Override
                public void onResponse(Call<ChartResponse> call, Response<ChartResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<OrderData> orderDataList = response.body().getOrders(); // Lấy danh sách đơn hàng từ phản hồi
                        Log.d("IncomeFragment", "Order Data Size: " + orderDataList.size()); // Kiểm tra kích thước dữ liệu
                        displayChart(orderDataList);
                    } else {
                        Log.e("IncomeFragment", "Response is not successful or body is null");
                    }
                }

                @Override
                public void onFailure(Call<ChartResponse> call, Throwable t) {
                    Log.e("IncomeFragment", "API call failed: " + t.getMessage());
                    t.printStackTrace(); // In ra stack trace để xem chi tiết lỗi
                }
            });
        } else {
            Log.e("IncomeFragment", "Store information not found in SharedPreferences");
        }
    }


    private void displayChart(List<OrderData> orderDataList) {
        List<BarEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        Random random = new Random();

        // Thêm dữ liệu vào danh sách entries
        for (int i = 0; i < orderDataList.size(); i++) {
            OrderData order = orderDataList.get(i);
            entries.add(new BarEntry(i, (float) order.getTotal())); // Tổng số tiền
            // Tạo màu ngẫu nhiên cho mỗi cột
            colors.add(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Total Income");
        dataSet.setColors(colors); // Màu sắc cho từng cột
        dataSet.setValueTextSize(12f); // Kích thước chữ hiển thị giá trị

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate(); // Refresh biểu đồ

        // Thiết lập trục X để hiển thị ngày
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int index = (int) value; // Không cần làm tròn, trực tiếp lấy index
                if (index >= 0 && index < orderDataList.size()) {
                    return orderDataList.get(index).getDate(); // Hiển thị ngày
                } else {
                    return "";
                }
            }
        });
        xAxis.setGranularity(1); // Đặt độ chia cho trục x
        xAxis.setLabelCount(orderDataList.size(), true); // Đảm bảo hiển thị tất cả nhãn
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt vị trí trục X nằm ở dưới

        // Thiết lập trục Y để hiển thị tổng tiền
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return String.valueOf(value); // Hiển thị giá trị tổng tiền
            }
        });
        yAxis.setDrawGridLines(true); // Hiển thị đường lưới cho trục Y
        yAxis.setGranularity(1f); // Đặt độ chia cho trục Y

        // Ẩn trục Y bên phải nếu không cần thiết
        barChart.getAxisRight().setEnabled(false);
    }
}
