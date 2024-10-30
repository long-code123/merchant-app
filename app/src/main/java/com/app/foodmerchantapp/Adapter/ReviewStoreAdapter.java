package com.app.foodmerchantapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.foodmerchantapp.R;
import com.app.foodmerchantapp.Model.ReviewStore;

import java.util.List;

public class ReviewStoreAdapter extends RecyclerView.Adapter<ReviewStoreAdapter.ReviewViewHolder> {

    private List<ReviewStore> reviews;
    private Context context;

    public ReviewStoreAdapter(Context context, List<ReviewStore> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reviewstore, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        // Get the review at the current position
        ReviewStore review = reviews.get(position);

        // Set the description and rating
        holder.textReviewStore.setText(review.getComment());
        holder.ratingStore.setText(String.valueOf(review.getRating()));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    // ViewHolder class for ReviewStore
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView textReviewStore;
        TextView ratingStore;
        ImageView imageStar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            textReviewStore = itemView.findViewById(R.id.textReviewStore);
            ratingStore = itemView.findViewById(R.id.ratingStore);
            imageStar = itemView.findViewById(R.id.imageView6);
        }
    }
}
