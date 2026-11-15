package com.yaniv.luz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidArgument(
            IllegalArgumentException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDatabaseConflict(
            DataIntegrityViolationException ex) {

        Throwable cause = ex;

        while (cause != null) {
            if (cause instanceof java.sql.SQLException sqlException
                    && "23P01".equals(sqlException.getSQLState())) {

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Time slot is already booked");
            }

            cause = cause.getCause();
        }

        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Database constraint violation"
        );
    }
}