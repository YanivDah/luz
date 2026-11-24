package com.yaniv.luz.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import com.yaniv.luz.repository.ServiceRepository;
import com.yaniv.luz.repository.BusinessOwnerRepository;
import com.yaniv.luz.model.Business;
import com.yaniv.luz.model.BusinessOwner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
import com.yaniv.luz.model.Service;
import com.yaniv.luz.dto.ServiceResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.AfterEach;

@ExtendWith(MockitoExtension.class)
class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private BusinessOwnerRepository businessOwnerRepository;

    @InjectMocks
    private ServiceService serviceService;

    @Test
    void shouldRejectUpdatingServiceOfAnotherBusiness() {

        Business business = mock(Business.class);
        when(business.getId()).thenReturn(1L);

        BusinessOwner owner = new BusinessOwner(
            business, "owner@luz.test", "test-hash", "Yaniv"
        );

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("owner@luz.test", null)
        );

        when(businessOwnerRepository.findByEmail("owner@luz.test"))
            .thenReturn(Optional.of(owner));

        when(serviceRepository.findByIdAndBusiness_Id(8L, 1L))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> serviceService.updateServicePrice(8L, 95));

        verify(serviceRepository).findByIdAndBusiness_Id(8L, 1L);
        verify(serviceRepository, never()).save(any());
    }

    @Test
    void shouldUpdatePriceForOwnBusiness() {

        Business business = mock(Business.class);
        when(business.getId()).thenReturn(1L);


        BusinessOwner owner = new BusinessOwner(
            business, "owner@luz.test", "test-hash", "Yaniv"
        );

        Service service = new Service(
            business, "Haircut", 30, 80);

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("owner@luz.test", null)
        );

        when(businessOwnerRepository.findByEmail("owner@luz.test"))
            .thenReturn(Optional.of(owner));

        when(serviceRepository.findByIdAndBusiness_Id(1L, 1L))
            .thenReturn(Optional.of(service));

        ServiceResponse response = serviceService.updateServicePrice(1L, 95);

        assertEquals(95, response.price());
        assertEquals(95, service.getPrice());
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}