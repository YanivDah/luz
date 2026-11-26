package com.yaniv.luz.controller;

import com.yaniv.luz.service.OwnerAuthService;
import com.yaniv.luz.dto.CurrentOwnerResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final OwnerAuthService ownerAuthService;

    public AuthController(OwnerAuthService ownerAuthService) {
        this.ownerAuthService = ownerAuthService;
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentOwnerResponse> getCurrentOwner() {
        CurrentOwnerResponse response = ownerAuthService.getCurrentOwner();
        return ResponseEntity.ok(response);
    }
}