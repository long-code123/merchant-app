package com.app.foodmerchantapp.Model;

public class ReviewStore {
    private Double rating;
    private String comment;
    private int userId;
    private int storeId;

    public ReviewStore() {
    }

    public ReviewStore(Double rating, String comment, int userId, int storeId) {
        this.rating = rating;
        this.comment = comment;
        this.userId = userId;
        this.storeId = storeId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "ReviewStore{" +
                "rating=" + rating +
                ", comment='" + comment + '\'' +
                ", userId=" + userId +
                ", storeId=" + storeId +
                '}';
    }
}
