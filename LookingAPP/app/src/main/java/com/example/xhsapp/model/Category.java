package com.example.xhsapp.model;

public class Category {
    public int id;
    public String name;
    public String iconUrl;
    public int count;

    public Category(int id, String name, String iconUrl, int count) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.count = count;
    }
}
