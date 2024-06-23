package com.example.ebay;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ProductModel {
    private String imageURL;
    private String title;

    private String itemId;

    private String zipCode;
    private String shippingCost;
    private String productCost;
    private String condition;

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItemID(String itemId) {
        this.itemId = itemId;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
    }

    public void setProductCost(String productCost) {
        this.productCost = productCost;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getImageUrl() {
        return imageURL;
    }

    public String getTitle() {
        return title;
    }

    public String getItemID() {
        return itemId;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public String getCondition() {
        return condition;
    }

    public String getPrice() {
        return productCost;
    }

}
