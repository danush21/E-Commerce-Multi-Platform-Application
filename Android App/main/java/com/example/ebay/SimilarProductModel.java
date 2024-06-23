package com.example.ebay;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class SimilarProductModel {
    private String imageURL;
    private String title;
    private String shippingCost;
    private String productCost;

    private String link;

    private String days;

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setlink(String link) {
        this.link = link;
    }

    public void setdays(String days) {
        this.days = days;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
    }

    public void setProductCost(String productCost) {
        this.productCost = productCost;
    }

    public String getImageUrl() {
        return imageURL;
    }

    public String getTitle() {
        return title;
    }

    public String getdays() {
        return days;
    }

    public String getlink() {
        return link;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public String getPrice() {
        return productCost;
    }

}
