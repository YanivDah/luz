package com.yaniv.luz.dto;

import java.time.OffsetDateTime;

public record AppointmentRequest(
    Long serviceId,
    OffsetDateTime startTime,
    String customerName,
    String customerPhone
) {
}