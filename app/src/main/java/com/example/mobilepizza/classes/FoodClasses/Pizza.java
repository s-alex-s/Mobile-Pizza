package com.example.mobilepizza.classes.FoodClasses;

import com.example.mobilepizza.classes.Food;

public class Pizza extends Food {

    private String dough_ru;
    private String dough_en;
    private String size;

    public Pizza() {}

    public Pizza(String description, String name, String key, String price, String img) {
        super(description, name, key, price, img);
    }

    public String getDough_en() {
        return dough_en;
    }

    public void setDough_en(String dough_en) {
        this.dough_en = dough_en;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDough_ru() {
        return dough_ru;
    }

    public void setDough_ru(String dough_ru) {
        this.dough_ru = dough_ru;
    }
}
