package com.example.customer_management.respository;

import com.example.customer_management.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
    Optional<Customer> findByEmail(String email);

    List<Customer> findByNameContainingIgnoreCase(String name);
}
