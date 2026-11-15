package com.yaniv.luz.repository;

import com.yaniv.luz.model.BusinessHours;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BusinessHoursRepository
        extends JpaRepository<BusinessHours, Long> {

    List<BusinessHours> findByBusiness_IdAndDayOfWeek(
        Long businessId,
        Integer dayOfWeek
    );
}