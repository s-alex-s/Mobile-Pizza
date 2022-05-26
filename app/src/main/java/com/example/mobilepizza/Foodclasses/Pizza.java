package com.example.mobilepizza.Foodclasses;

public class Pizza {
    private String name;
    private String composition;
    private String price;
    private int size;

    public Pizza() {}

    public Pizza(String name, String composition, String price, int size, int quantity) {
        this.name = name;
        this.composition = composition;
        this.price = price;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
