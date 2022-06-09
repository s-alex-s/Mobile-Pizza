package com.example.mobilepizza.classes;

import java.util.Objects;

public class CartItems {
    private String name_ru, name_en;
    private int price;
    private String settings_ru, settings_en;
    private String img;
    private int quantity = 1;
    private String key;

    public CartItems() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItems cartItems = (CartItems) o;
        return price == cartItems.price && Objects.equals(name_ru, cartItems.name_ru) && Objects.equals(name_en, cartItems.name_en) && Objects.equals(settings_ru, cartItems.settings_ru) && Objects.equals(settings_en, cartItems.settings_en) && Objects.equals(img, cartItems.img);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name_ru, name_en, price, settings_ru, settings_en, img);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSettings_en() {
        return settings_en;
    }

    public void setSettings_en(String settings_en) {
        this.settings_en = settings_en;
    }

    public String getSettings_ru() {
        return settings_ru;
    }

    public void setSettings_ru(String settings_ru) {
        this.settings_ru = settings_ru;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
