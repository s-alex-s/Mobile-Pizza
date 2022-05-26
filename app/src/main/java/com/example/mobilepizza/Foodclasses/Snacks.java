package com.example.mobilepizza.Foodclasses;

public class Snacks {
    private String name;
    private String price;

    public Snacks() {}

    public Snacks(String name, String volume, String price, int size, int quantity) {
        this.name = name;
        this.price = price;
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

}
