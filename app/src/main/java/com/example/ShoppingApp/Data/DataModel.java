package com.example.ShoppingApp.Data;

public class DataModel {
    private String productName;
    private String productPrice;
    private int id_;
    private int image;
    private int quantity;

    public DataModel(String productName, String productPrice, int id_, int image) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.id_ = id_;
        this.image = image;
        this.quantity = 0;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
    public int getId_() {
        return id_;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }
}