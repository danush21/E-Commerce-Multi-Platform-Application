package com.example.ebay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductModel> productList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener =  listener;
    }

    public ProductAdapter(List<ProductModel> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product = productList.get(position);

        Picasso.get().load(product.getImageUrl()).into(holder.imageViewProduct);
        holder.textViewTitle.setText(product.getTitle());
        holder.textViewZipCode.setText(product.getZipCode());
        holder.textViewShippingCost.setText(product.getShippingCost());
        holder.textViewCondition.setText(product.getCondition());
        holder.textViewPrice.setText(product.getPrice());

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(position);
            }
        });
//
//        // Handle add to wishlist button click here
//        holder.buttonAddToWishlist.setOnClickListener(v -> {
//            // Perform action on button click
//            // For example: Add to wishlist functionality
//        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // View holder for your product item
    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewProduct;
        TextView textViewTitle, textViewZipCode, textViewShippingCost, textViewCondition, textViewPrice;
        AppCompatImageButton buttonAddToWishlist;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textTitle);
            textViewZipCode = itemView.findViewById(R.id.textZipCode);
            textViewShippingCost = itemView.findViewById(R.id.textShippingCost);
            textViewCondition = itemView.findViewById(R.id.textCondition);
            textViewPrice = itemView.findViewById(R.id.textPrice);
            buttonAddToWishlist = itemView.findViewById(R.id.buttonWishlist);
        }
    }
}