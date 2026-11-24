package com.yaniv.luz.controller;

import com.yaniv.luz.service.AppointmentService;
import com.yaniv.luz.dto.AppointmentRequest;
import com.yaniv.luz.dto.AppointmentResponse;
import com.yaniv.luz.dto.OwnerAppointmentResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;

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

        AppointmentResponse response = appointmentService.createAppointment(
                businessId,
                request.serviceId(),
                request.startTime().toInstant(),
                request.customerName(),
                request.customerPhone()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OwnerAppointmentResponse>> getAppointmentsForBusiness(
            @PathVariable Long businessId) {

        List<OwnerAppointmentResponse> response =
                appointmentService.getAppointmentsForBusiness(businessId);

        return ResponseEntity.ok(response);
    }
}