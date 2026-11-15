package com.yaniv.luz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TimeSlotConflictException extends RuntimeException {

    public TimeSlotConflictException(String message) {
        super(message);
    }
}