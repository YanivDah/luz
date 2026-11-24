package com.yaniv.luz.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;

import com.yaniv.luz.repository.BusinessRepository;
import com.yaniv.luz.repository.ServiceRepository;
import com.yaniv.luz.repository.BusinessHoursRepository;
import com.yaniv.luz.repository.AppointmentRepository;
import java.time.LocalDate;
import com.yaniv.luz.model.Business;
import static org.mockito.Mockito.*;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import java.util.Optional;
import com.yaniv.luz.model.Service;
import java.time.LocalTime;
import java.util.List;
import com.yaniv.luz.model.BusinessHours;
import com.yaniv.luz.model.Appointment;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private BusinessHoursRepository businessHoursRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private AvailabilityService availabilityService;

    @Test
    void shouldExcludeOverlappingAppointments() {

        Clock fixedClock = Clock.fixed(
            Instant.parse("2026-11-16T10:00:00Z"),
            ZoneOffset.UTC
        );

        when(clock.instant()).thenReturn(fixedClock.instant());

        LocalDate date = LocalDate.of(2026, 11, 20);

        Business business = mock(Business.class);
        when(business.getId()).thenReturn(1L);
        when(business.getTimezone()).thenReturn("Asia/Jerusalem");
        when(businessRepository.findById(1L))
            .thenReturn(Optional.of(business));

        Service service = mock(Service.class);

        when(service.getBusiness()).thenReturn(business);
        when(service.isActive()).thenReturn(true);
        when(service.getDurationMinutes()).thenReturn(30);

        when(serviceRepository.findById(1L))
            .thenReturn(Optional.of(service));

        BusinessHours hours = mock(BusinessHours.class);

        when(hours.getOpenTime()).thenReturn(LocalTime.of(9, 0));
        when(hours.getCloseTime()).thenReturn(LocalTime.of(18, 0));

        when(businessHoursRepository.findByBusiness_IdAndDayOfWeek(1L, 5))
            .thenReturn(List.of(hours));

        Appointment appointment = mock(Appointment.class);

        Instant appointmentStart = Instant.parse("2026-11-20T08:00:00Z");
        Instant appointmentEnd = Instant.parse("2026-11-20T08:30:00Z");

        when(appointment.getStartTime()).thenReturn(appointmentStart);
        when(appointment.getEndTime()).thenReturn(appointmentEnd);
        when(appointmentRepository
            .findByBusiness_IdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
                eq(1L),
                eq(Appointment.AppointmentStatus.CONFIRMED),
                any(Instant.class),
                any(Instant.class)
            )
        ).thenReturn(List.of(appointment));

        List<Instant> availableSlots =
                availabilityService.getAvailableTimeSlots(
                    1L, 1L, date
        );

        assertTrue(availableSlots.contains(
            Instant.parse("2026-11-20T07:30:00Z")
        ));

        assertFalse(availableSlots.contains(
            Instant.parse("2026-11-20T07:45:00Z")
        ));

        assertFalse(availableSlots.contains(
            Instant.parse("2026-11-20T08:00:00Z")
        ));

        assertFalse(availableSlots.contains(
            Instant.parse("2026-11-20T08:15:00Z")
        ));

        assertTrue(availableSlots.contains(
            Instant.parse("2026-11-20T08:30:00Z")
        ));
    }
}