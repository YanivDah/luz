package com.yaniv.luz.service;

import com.yaniv.luz.repository.BusinessHoursRepository;
import com.yaniv.luz.repository.AppointmentRepository;
import com.yaniv.luz.repository.ServiceRepository;
import com.yaniv.luz.repository.BusinessRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Instant;
import java.time.Clock;
import java.util.List;
import java.util.ArrayList;
import com.yaniv.luz.model.Business;
import java.time.ZoneId;
import com.yaniv.luz.model.BusinessHours;
import java.time.LocalTime;
import java.time.LocalDateTime;
import com.yaniv.luz.model.Appointment;
import com.yaniv.luz.exception.ResourceNotFoundException;
import java.time.temporal.ChronoUnit;

@Service
public class AvailabilityService {

    private final BusinessHoursRepository businessHoursRepository;
    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final BusinessRepository businessRepository;
    private final Clock clock;

    public AvailabilityService(
            BusinessHoursRepository businessHoursRepository,
            AppointmentRepository appointmentRepository,
            ServiceRepository serviceRepository,
            BusinessRepository businessRepository,
            Clock clock) {

        this.businessHoursRepository = businessHoursRepository;
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.businessRepository = businessRepository;
        this.clock = clock;
    }

    public List<Instant> getAvailableTimeSlots(Long businessId, Long serviceId, LocalDate date) {

        List<Instant> availableSlots = new ArrayList<>();
        
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        
        ZoneId zoneId = ZoneId.of(business.getTimezone());

        int dayOfWeek = date.getDayOfWeek().getValue();
        
        var service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        long serviceDuration = service.getDurationMinutes();

        if (!service.getBusiness().getId().equals(businessId) || !service.isActive()) {
            throw new ResourceNotFoundException("Service not found");
        }
        
        List<BusinessHours> businessHoursList = businessHoursRepository
                .findByBusiness_IdAndDayOfWeek(businessId, dayOfWeek);

        if (businessHoursList.isEmpty()) {
            return availableSlots;
        }
    
        Instant dayStart = date.atStartOfDay(zoneId).toInstant();
        Instant dayEnd = date.plusDays(1).atStartOfDay(zoneId).toInstant();

        List<Appointment> existingAppointments =
                appointmentRepository
                    .findByBusiness_IdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
                        businessId,
                        Appointment.AppointmentStatus.CONFIRMED,
                        dayEnd,
                        dayStart
                    );
        
        Instant now = Instant.now(clock);
        Instant earliestBooking = now.plus(60, ChronoUnit.MINUTES);
        Instant latestBooking = now.plus(30, ChronoUnit.DAYS);

        for (BusinessHours hours : businessHoursList) {
            LocalTime current = hours.getOpenTime();
            LocalTime closingTime = hours.getCloseTime();

            int minutes = current.getMinute();
            int remainder = minutes % 15;
            if (remainder != 0) {
                current = current.plusMinutes(15 - remainder);
            }

            Instant currentTime = LocalDateTime.of(date, current).atZone(zoneId).toInstant();
            Instant endTime = LocalDateTime.of(date, closingTime).atZone(zoneId).toInstant();

            while (!currentTime.plus(serviceDuration, ChronoUnit.MINUTES).isAfter(endTime)) {

                Instant candidateEnd = currentTime.plus(serviceDuration, ChronoUnit.MINUTES);
                boolean hasConflict = false;
                for (Appointment appointment : existingAppointments) {
                    if (currentTime.isBefore(appointment.getEndTime()) &&
                        candidateEnd.isAfter(appointment.getStartTime())) {
                        hasConflict = true;
                        break;
                    }
                }
                if (!hasConflict && !currentTime.isBefore(earliestBooking) && !currentTime.isAfter(latestBooking)) {
                    availableSlots.add(currentTime);
                }
                currentTime = currentTime.plus(15, ChronoUnit.MINUTES);
            }
        }

        return availableSlots;
    }
}