package com.yaniv.luz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name = "businesses")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "timezone", nullable = false)
    private String timezone;

    public Business(String name, String slug, String timezone) {
        this.name = name;
        this.slug = slug;
        this.timezone = timezone;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;   
    }

    public String getSlug() {
        return slug;
    }

    public String getTimezone() {
        return timezone;
    }

    protected Business() {
        // Default constructor for JPA
    }
}