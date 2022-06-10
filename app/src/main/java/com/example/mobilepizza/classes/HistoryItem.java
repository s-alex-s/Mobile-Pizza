package com.example.mobilepizza.classes;

import java.util.ArrayList;

public class HistoryItem {

    private String key;
    private String time;
    private String address;
    private ArrayList<String> images;
    private ArrayList<CartItems> cartItems;
    private int sum;

    public HistoryItem() {}

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public ArrayList<CartItems> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartItems> cartItems) {
        this.cartItems = cartItems;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
