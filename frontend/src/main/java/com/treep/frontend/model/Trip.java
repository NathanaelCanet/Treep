package com.treep.frontend.model;

import java.util.ArrayList;
import java.util.List;

public class Trip {
    private String id;
    private String destination;
    private double price;
    private String date;
    private List<Activity> activities = new ArrayList<>();

    public Trip(String id, String destination, double price, String date, List<Activity> activities) {
        this.id = id;
        this.destination = destination;
        this.price = price;
        this.date = date;
        this.activities = activities;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}