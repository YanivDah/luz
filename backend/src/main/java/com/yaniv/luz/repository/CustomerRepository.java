package com.yaniv.luz.repository;

import com.yaniv.luz.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByBusiness_IdAndPhone(Long businessId, String phone);

}