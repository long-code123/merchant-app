package com.app.foodmerchantapp.Model;

import java.util.List;

public class Order {
    private int orderId;
    private String deliveryTime;
    private int userId;
    private Integer shipperId;
    private String status;
    private int storeId;
    private String createdAt;
    private String updatedAt;
    private List<OrderItem> items;

    // Constructor
    public Order(int orderId, String deliveryTime, int userId, Integer shipperId, String status, int storeId, String createdAt, String updatedAt, List<OrderItem> items) {
        this.orderId = orderId;
        this.deliveryTime = deliveryTime;
        this.userId = userId;
        this.shipperId = shipperId;
        this.status = status;
        this.storeId = storeId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = items;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getShipperId() {
        return shipperId;
    }

    public void setShipperId(Integer shipperId) {
        this.shipperId = shipperId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}