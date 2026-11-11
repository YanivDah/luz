package com.yaniv.luz.security;

import com.yaniv.luz.repository.BusinessOwnerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import com.yaniv.luz.model.BusinessOwner;

@Service
public class BusinessOwnerDetailsService implements UserDetailsService {

private final BusinessOwnerRepository businessOwnerRepository;

    public BusinessOwnerDetailsService(BusinessOwnerRepository businessOwnerRepository) {
        this.businessOwnerRepository = businessOwnerRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        BusinessOwner owner = businessOwnerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Business owner not found"));

        return User.withUsername(owner.getEmail())
                .password(owner.getPasswordHash())
                .roles("OWNER")
                .build();
    }
}
