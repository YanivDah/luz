package com.yaniv.luz.repository;

import com.yaniv.luz.model.BusinessOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BusinessOwnerRepository extends JpaRepository<BusinessOwner, Long> {
    Optional<BusinessOwner> findByEmail(String email);
}