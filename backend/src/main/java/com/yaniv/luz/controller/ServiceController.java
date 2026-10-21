package com.yaniv.luz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.yaniv.luz.model.Service;
import com.yaniv.luz.service.ServiceService;
import java.util.List;

@RestController
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }
    @GetMapping("/api/businesses/{businessId}/services")
    public List<Service> getServices(@PathVariable Long businessId) {
        return serviceService.getServices();
    }
}