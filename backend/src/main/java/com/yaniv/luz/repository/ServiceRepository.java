package com.yaniv.luz.repository;

import com.yaniv.luz.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByBusiness_IdAndIsActiveTrue(Long businessId);
    Optional<Service> findByIdAndBusiness_Id(Long serviceId, Long businessId);
}