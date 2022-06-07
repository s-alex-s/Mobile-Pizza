package com.example.mobilepizza.classes.FoodClasses;

import com.example.mobilepizza.classes.Food;

public class Pizza extends Food {

    private String dough_ru;
    private String dough_en;
    private String size;
    private String price_small;
    private String price_medium;
    private String price_big;

    public Pizza() {}

    public Pizza(String description_ru, String description_en, String name, String key, String img) {
        super(description_ru, description_en, name, key, img);
    }

    public String getPrice_big() {
        return price_big;
    }

    public void setPrice_big(String price_big) {
        this.price_big = price_big;
    }

    public String getPrice_medium() {
        return price_medium;
    }

    public void setPrice_medium(String price_medium) {
        this.price_medium = price_medium;
    }

    public String getPrice_small() {
        return price_small;
    }

    public void setPrice_small(String price_small) {
        this.price_small = price_small;
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
