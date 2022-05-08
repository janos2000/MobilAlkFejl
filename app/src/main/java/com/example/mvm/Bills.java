package com.example.mvm;


public class Bills {
    private String name;
    private String date;
    private String price;
    private int image;

    public Bills() {
    }

    public Bills(String name, String date, String price, int image) {
        this.name = name;
        this.date = date;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }
}
