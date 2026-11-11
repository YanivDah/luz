package com.yaniv.luz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.yaniv.luz.model.Service;
import com.yaniv.luz.dto.ServiceResponse;
import com.yaniv.luz.service.ServiceService;
import java.util.List;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@RestController
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/api/businesses/{businessId}/services")
    public List<ServiceResponse> getServices(@PathVariable Long businessId) {
        return serviceService.getServices(businessId);
    }

    @PatchMapping("/api/services/{serviceId}/price")
    public ServiceResponse updateServicePrice(
            @PathVariable Long serviceId,
            @RequestBody Map<String, Integer> body) {

        Integer newPrice = body.get("price");

        return serviceService.updateServicePrice(serviceId, newPrice);
    }
}