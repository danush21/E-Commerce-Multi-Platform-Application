package com.example.ebay;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso; // Use your preferred image loading library

import java.util.List;

public class SimilarProductsAdapter extends RecyclerView.Adapter<SimilarProductsAdapter.ProductViewHolder> {

    private List<SimilarProductModel> similarproductList; // Assuming you have a Product class with necessary fields
    private Context context;

    public SimilarProductsAdapter(List<SimilarProductModel> similarproductList, Context context) {
        this.similarproductList = similarproductList;
        this.context = context;
        Log.d("size", String.valueOf(similarproductList.size()));
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_similar_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        SimilarProductModel product = similarproductList.get(position);

        // Bind data to views
        holder.titleTextView.setText(product.getTitle());
        holder.shippingCostTextView.setText("Shipping Cost: $" + product.getShippingCost());
        holder.daysLeftTextView.setText("Days Left: " + product.getdays());
        holder.priceTextView.setText("Price: $" + product.getPrice());

        // Load image using Picasso (or your preferred image loading library)
        Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.ic_shopping).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(similarproductList == null)
                return 0;
        return similarproductList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView, shippingCostTextView, daysLeftTextView, priceTextView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_product);
            titleTextView = itemView.findViewById(R.id.textView_title);
            shippingCostTextView = itemView.findViewById(R.id.textView_shipping_cost);
            daysLeftTextView = itemView.findViewById(R.id.textView_days_left);
            priceTextView = itemView.findViewById(R.id.textView_price);
        }
    }
}
