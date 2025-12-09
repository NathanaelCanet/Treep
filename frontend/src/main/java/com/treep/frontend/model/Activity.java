package com.treep.frontend.model;

public class Activity {
    private String name;
    private double price;

    public Activity(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // Getters et Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}