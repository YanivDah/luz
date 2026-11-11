package com.yaniv.luz.service;

import java.util.List;
import com.yaniv.luz.model.Service;
import com.yaniv.luz.repository.ServiceRepository;
import com.yaniv.luz.dto.ServiceResponse;
import com.yaniv.luz.repository.BusinessOwnerRepository;
import com.yaniv.luz.model.BusinessOwner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final BusinessOwnerRepository businessOwnerRepository;

    public ServiceService(ServiceRepository serviceRepository, BusinessOwnerRepository businessOwnerRepository) {
        this.serviceRepository = serviceRepository;
        this.businessOwnerRepository = businessOwnerRepository;  
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

    private Long getAuthenticatedBusinessId() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        BusinessOwner owner = businessOwnerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated owner not found"));

        return owner.getBusiness().getId();
    }

    @Transactional
    public ServiceResponse updateServicePrice(Long serviceId, Integer newPrice) {

        Long businessId = getAuthenticatedBusinessId();

        Service service = serviceRepository
        .findByIdAndBusiness_Id(serviceId, businessId)
        .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        if (newPrice == null) {
            throw new IllegalArgumentException("Price is required");
        }
        service.updatePrice(newPrice);

        return new ServiceResponse(
            service.getId(),
            service.getName(),
            service.getDurationMinutes(),
            service.getPrice()
        );
    }
}