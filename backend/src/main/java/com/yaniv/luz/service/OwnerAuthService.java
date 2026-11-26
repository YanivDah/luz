package com.yaniv.luz.service;

import com.yaniv.luz.repository.BusinessOwnerRepository;
import org.springframework.stereotype.Service;
import com.yaniv.luz.dto.CurrentOwnerResponse;
import com.yaniv.luz.model.BusinessOwner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OwnerAuthService {

    private final BusinessOwnerRepository businessOwnerRepository;

    public OwnerAuthService(BusinessOwnerRepository businessOwnerRepository) {
        this.businessOwnerRepository = businessOwnerRepository;
    }
    
    public CurrentOwnerResponse getCurrentOwner() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Not authenticated"
            );
        }

        String email = authentication.getName();

        BusinessOwner owner = businessOwnerRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Business owner not found"
                ));

        return new CurrentOwnerResponse(
                owner.getBusiness().getId(),
                owner.getEmail()
        );
    }
}