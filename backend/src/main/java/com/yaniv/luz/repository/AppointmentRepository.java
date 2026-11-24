package com.yaniv.luz.repository;

import com.yaniv.luz.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByBusiness_IdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
        Long businessId,
        Appointment.AppointmentStatus status,
        Instant endTime,
        Instant startTime
    );

    List<Appointment> findByBusiness_IdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
        Long businessId,
        Appointment.AppointmentStatus status,
        Instant endTime,
        Instant startTime
    );

    List<Appointment> findByBusiness_IdOrderByStartTimeAsc(Long businessId);
}