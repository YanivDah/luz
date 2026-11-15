package com.yaniv.luz.controller;

import com.yaniv.luz.service.AppointmentService;
import com.yaniv.luz.dto.AppointmentRequest;
import com.yaniv.luz.dto.AppointmentResponse;
import com.yaniv.luz.model.Appointment;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/businesses/{businessId}/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @PathVariable Long businessId,
            @RequestBody AppointmentRequest request) {
                Appointment appointment = appointmentService.createAppointment(
                    businessId,
                    request.serviceId(),
                    request.startTime().toInstant(),
                    request.customerName(),
                    request.customerPhone()
                );
                AppointmentResponse response = new AppointmentResponse(
                    appointment.getId(),
                    appointment.getService().getId(),
                    appointment.getStartTime(),
                    appointment.getEndTime(),
                    appointment.getPriceAtBooking(),
                    appointment.getStatus().name()
                );
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
}