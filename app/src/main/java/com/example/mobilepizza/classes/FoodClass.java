package com.example.mobilepizza.classes;

public class FoodClass {
    private String description_en;
    private String description_ru;
    private String name_en;
    private String name_ru;
    private String key;
    private String price;
    private int img;

    public FoodClass() {}

    public FoodClass(String description, String name, String key, String price, int img) {
        this.description_en = description;
        this.description_ru = description;
        this.name_en = name;
        this.name_ru = name;
        this.key =  key;
        this.price = price;
        this.img = img;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getDescription_ru() {
        return description_ru;
    }

    public void setDescription_ru(String description_ru) {
        this.description_ru = description_ru;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_ru() {
        return name_ru;
    }

    public void setName_ru(String name_ru) {
        this.name_ru = name_ru;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
