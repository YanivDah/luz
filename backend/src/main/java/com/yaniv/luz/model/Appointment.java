package com.yaniv.luz.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_phone", nullable = false)
    private String customerPhone;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Column(name = "price_at_booking", nullable = false)
    private int priceAtBooking;

    public enum AppointmentStatus {
        CONFIRMED, CANCELLED
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Appointment(Business business, Service service, Customer customer,
                       String customerName, String customerPhone,
                       Instant startTime, Instant endTime,
                       int priceAtBooking) {
        this.business = business;
        this.service = service;
        this.customer = customer;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priceAtBooking = priceAtBooking;
        this.status = AppointmentStatus.CONFIRMED;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Business getBusiness() {
        return business;
    }

    public Service getService() {
        return service;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public int getPriceAtBooking() {
        return priceAtBooking;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    protected Appointment() {
        // Default constructor for JPA
    }
}