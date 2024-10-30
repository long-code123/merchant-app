package com.app.foodmerchantapp.Model;

import java.util.List;

public class ChartResponse {
    private List<OrderData> orders;

    // Getter và Setter
    public List<OrderData> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderData> orders) {
        this.orders = orders;
    }
}
