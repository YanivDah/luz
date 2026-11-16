package com.yaniv.luz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.yaniv.luz.service.AvailabilityService;
import java.util.List;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
public class AvailabilityController {

    private final AvailabilityService availabilityService;
    
    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/api/businesses/{businessId}/services/{serviceId}/availability") 
    public List<Instant> getAvailability(@PathVariable Long businessId, @PathVariable Long serviceId,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return availabilityService.getAvailableTimeSlots(businessId, serviceId, date);
    }
}