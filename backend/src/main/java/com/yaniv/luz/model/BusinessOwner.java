package com.yaniv.luz.model;

import jakarta.persistence.*;

@Entity
@Table(name = "business_owners")
public class BusinessOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false, unique = true)
    private Business business;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "name", nullable = false)
    private String name;

    public BusinessOwner(Business business, String email, String passwordHash, String name) {
        this.business = business;
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Business getBusiness() {
        return business;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    protected BusinessOwner() {
        // Default constructor for JPA
    }
}