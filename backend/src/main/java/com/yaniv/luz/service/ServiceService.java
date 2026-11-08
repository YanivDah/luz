package com.yaniv.luz.service;

import java.util.List;
import com.yaniv.luz.model.Service;
import com.yaniv.luz.repository.ServiceRepository;
import com.yaniv.luz.dto.ServiceResponse;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<ServiceResponse> getServices(Long businessId) {
        return serviceRepository.findByBusiness_IdAndIsActiveTrue(businessId)
            .stream()
            .map(service -> new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getDurationMinutes(),
                service.getPrice()
            ))
            .toList();  
    }
}