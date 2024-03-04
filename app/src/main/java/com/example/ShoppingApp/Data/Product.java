package com.example.ShoppingApp.Data;

public class Product {
    private String name;

    private String price;

    private int imageDrawable;

    private String id;

    public Product(String name, String price, int imageDrawable, String id) {
        this.name = name;
        this.price = price;
        this.imageDrawable = imageDrawable;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}