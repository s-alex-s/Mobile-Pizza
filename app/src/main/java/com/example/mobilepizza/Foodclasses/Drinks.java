package com.example.mobilepizza.Foodclasses;

public class Drinks {
    private String name;
    private String volume;
    private String price;

    public Drinks() {}

    public Drinks(String name, String volume, String price, int size, int quantity) {
        this.name = name;
        this.volume = volume;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
