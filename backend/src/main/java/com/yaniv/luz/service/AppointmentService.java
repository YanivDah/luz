package com.yaniv.luz.service;

import com.yaniv.luz.repository.AppointmentRepository;
import com.yaniv.luz.repository.CustomerRepository;
import com.yaniv.luz.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import com.yaniv.luz.model.Customer;
import com.yaniv.luz.model.Appointment;
import com.yaniv.luz.exception.TimeSlotConflictException;
import com.yaniv.luz.exception.ResourceNotFoundException;
import com.yaniv.luz.repository.BusinessHoursRepository;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import com.yaniv.luz.model.BusinessHours;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final ServiceRepository serviceRepository;
    private final BusinessHoursRepository businessHoursRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            CustomerRepository customerRepository,
            ServiceRepository serviceRepository,
            BusinessHoursRepository businessHoursRepository) {

        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.serviceRepository = serviceRepository;
        this.businessHoursRepository = businessHoursRepository;
    }

    @Transactional
    public Appointment createAppointment(Long businessId, Long serviceId,
                                  Instant startTime, String customerName, String customerPhone) {

        com.yaniv.luz.model.Service service = serviceRepository
        .findByIdAndBusiness_IdAndIsActiveTrue(serviceId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        Instant endTime = startTime.plus(service.getDurationMinutes(), ChronoUnit.MINUTES);
        Instant now = Instant.now();
        Instant earliest = now.plus(60, ChronoUnit.MINUTES);
        Instant latest = now.plus(30, ChronoUnit.DAYS);

        ZoneId businessZone = ZoneId.of(service.getBusiness().getTimezone());
        ZonedDateTime localStart = startTime.atZone(businessZone);
        ZonedDateTime localEnd = endTime.atZone(businessZone);
        int dayOfWeek = localStart.getDayOfWeek().getValue();

        List<BusinessHours> workingHours = 
        businessHoursRepository.findByBusiness_IdAndDayOfWeek(businessId, dayOfWeek);

        if (startTime.isBefore(earliest) || startTime.isAfter(latest)) {
            throw new IllegalArgumentException("Appointment time must be between 1 hour and 30 days from now");
        }

        if (!localStart.toLocalDate().equals(localEnd.toLocalDate())) {
            throw new IllegalArgumentException(
                "Appointment cannot cross midnight"
            );
        }

        boolean withinWorkingHours = workingHours.stream().anyMatch(hours ->
            !localStart.toLocalTime().isBefore(hours.getOpenTime()) &&
            !localEnd.toLocalTime().isAfter(hours.getCloseTime())
        );

        if (!withinWorkingHours) {
            throw new IllegalArgumentException(
                "Appointment is outside business working hours"
            );
        }

        if (appointmentRepository.existsByBusiness_IdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
            businessId, Appointment.AppointmentStatus.CONFIRMED, endTime, startTime
        )) {
            throw new TimeSlotConflictException("Time slot is already booked");
        }

        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (customerPhone == null || customerPhone.isBlank()) {
            throw new IllegalArgumentException("Customer phone is required");
        }
        String normalizedPhone = normalizePhone(customerPhone);
        Customer customer = customerRepository
                .findByBusiness_IdAndPhone(businessId, normalizedPhone)
                .orElseGet(() -> {
                    Customer newCustomer = new Customer(
                    service.getBusiness(),
                    customerName,
                    normalizedPhone);

                    return customerRepository.save(newCustomer);
                });
        Appointment appointment = new Appointment(service.getBusiness(), service, customer,
            customerName, normalizedPhone, startTime, endTime, service.getPrice());
        
        return appointmentRepository.save(appointment);
    }

    private String normalizePhone(String phone) {
        String normalized = phone.replace("-", "").replace(" ", "");
        if (normalized.startsWith("0")) {
            normalized = "+972" + normalized.substring(1);
        }
        if (!normalized.matches("\\+9725\\d{8}")) {
            throw new IllegalArgumentException("Invalid Israeli mobile phone number");
        }
        return normalized;
    }
}