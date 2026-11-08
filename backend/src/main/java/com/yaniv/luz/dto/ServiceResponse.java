package com.yaniv.luz.dto;

public record ServiceResponse(
    Long id,
    String name,
    int durationMinutes,
    int price
) {
}