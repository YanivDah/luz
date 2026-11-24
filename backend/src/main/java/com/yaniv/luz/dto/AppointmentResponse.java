package com.yaniv.luz.dto;

import java.time.Instant;

public record AppointmentResponse(
    Long id,
    Long serviceId,
    String serviceName,
    Instant startTime,
    Instant endTime,
    int priceAtBooking,
    String status
) {
}