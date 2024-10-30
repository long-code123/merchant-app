package com.app.foodmerchantapp.Model;

public class OrderData {
    private String date;  // Định nghĩa kiểu dữ liệu của date là String
    private double total;  // Kiểu dữ liệu tổng phải là double

    public String getDate() {
        return date;
    }

    public double getTotal() {
        return total; // Phải đảm bảo kiểu dữ liệu trả về là double
    }
}
