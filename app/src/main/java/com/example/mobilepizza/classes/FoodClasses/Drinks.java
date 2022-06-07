package com.example.mobilepizza.classes.FoodClasses;

import com.example.mobilepizza.classes.Food;

public class Drinks extends Food {
    private String price;

    public Drinks() {}

    public Drinks(String description_ru, String description_en, String name, String key, String img) {
        super(description_ru, description_en, name, key, img);
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
