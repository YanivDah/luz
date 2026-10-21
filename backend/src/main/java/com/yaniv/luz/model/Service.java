package com.yaniv.luz.model;

public class Service {

    private Long id;
    private String name;
    private int durationMinutes;
    private int price;

    public Service(Long id, String name, int durationMinutes, int price) {
        this.id = id;
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getPrice() {
        return price;
    }
}