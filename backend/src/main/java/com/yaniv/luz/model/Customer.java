package com.yaniv.luz.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    public Customer(Business business, String name, String phone) {
        this.business = business;
        this.name = name;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }     

    public Business getBusiness() {
        return business;
    }      

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }   

    protected Customer() {
        // Default constructor for JPA
    }
}