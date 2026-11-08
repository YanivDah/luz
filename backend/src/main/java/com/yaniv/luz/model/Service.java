package com.yaniv.luz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public Service(Business business, String name, int durationMinutes, int price) {
        this.business = business;
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.isActive = true; // Default to active when created
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

    public Business getBusiness() {
        return business;
    }

    public boolean isActive() {
        return isActive;
    }

    protected Service() {
        // Default constructor for JPA
    }
}