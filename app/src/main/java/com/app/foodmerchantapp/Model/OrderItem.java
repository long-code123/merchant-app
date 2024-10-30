package com.app.foodmerchantapp.Model;

public class OrderItem {
    private int foodQuantityId;
    private int orderId;
    private int foodId;
    private int quantity;
    private String createdAt;
    private String updatedAt;
    private Food food;

    // Constructor
    public OrderItem(int foodQuantityId, int orderId, int foodId, int quantity, String createdAt, String updatedAt, Food food) {
        this.foodQuantityId = foodQuantityId;
        this.orderId = orderId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.food = food;
    }

    // Getters and Setters
    public int getFoodQuantityId() {
        return foodQuantityId;
    }

    public void setFoodQuantityId(int foodQuantityId) {
        this.foodQuantityId = foodQuantityId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
