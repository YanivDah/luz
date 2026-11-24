package com.yaniv.luz.dto;

import java.time.Instant;
import java.math.BigDecimal;

public record OwnerAppointmentResponse(
        Long id,
        String customerName,
        String customerPhone,
        String serviceName,
        Instant startTime,
        Instant endTime,
        int priceAtBooking,
        String status
) {}