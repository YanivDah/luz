package com.yaniv.luz.service;

import com.yaniv.luz.repository.AppointmentRepository;
import com.yaniv.luz.repository.CustomerRepository;
import com.yaniv.luz.repository.ServiceRepository;
import com.yaniv.luz.repository.BusinessHoursRepository;
import com.yaniv.luz.repository.BusinessOwnerRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.yaniv.luz.model.Business;
import com.yaniv.luz.model.BusinessOwner;
import org.junit.jupiter.api.AfterEach;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private BusinessHoursRepository businessHoursRepository;

    @Mock
    private BusinessOwnerRepository businessOwnerRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void shouldRejectAccessToAnotherBusiness() {
        // Arrange
        Long requestedBusinessId = 2L;
        String ownerEmail = "owner@example.com";

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        ownerEmail, null, java.util.List.of()
                )
        );

        Business business = mock(Business.class);
        BusinessOwner owner = mock(BusinessOwner.class);

        when(business.getId()).thenReturn(1L);
        when(owner.getBusiness()).thenReturn(business);
        when(businessOwnerRepository.findByEmail(ownerEmail))
                .thenReturn(Optional.of(owner));

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> appointmentService.getAppointmentsForBusiness(requestedBusinessId)
        );

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        verify(appointmentRepository, never())
                .findByBusiness_IdOrderByStartTimeAsc(anyLong());
    }

    @Test
    void shouldAllowOwnerToAccessOwnAppointments() {
        // Arrange
        Long businessId = 1L;
        String ownerEmail = "owner@example.com";

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        ownerEmail, null, java.util.List.of()
                )
        );

        Business business = mock(Business.class);
        BusinessOwner owner = mock(BusinessOwner.class);

        when(business.getId()).thenReturn(businessId);
        when(owner.getBusiness()).thenReturn(business);
        when(businessOwnerRepository.findByEmail(ownerEmail))
                .thenReturn(Optional.of(owner));

        when(appointmentRepository.findByBusiness_IdOrderByStartTimeAsc(businessId))
                .thenReturn(java.util.List.of());

        // Act
        var appointments = appointmentService.getAppointmentsForBusiness(businessId);

        // Assert
        assertTrue(appointments.isEmpty());

        verify(appointmentRepository)
                .findByBusiness_IdOrderByStartTimeAsc(businessId);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
}