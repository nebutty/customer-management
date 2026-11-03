package com.example.customer_management.respository;

import com.example.customer_management.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
class CustomerRepositoryTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("customer_test")
                    .withUsername("test_user")
                    .withPassword("test_password");

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findByEmail_shouldReturnCustomer_whenEmailExists() {
        Customer customer = new Customer(
                "Abebe Kebede", "abebe@example.com");

        customerRepository.saveAndFlush(customer);

        var result = customerRepository.findByEmail(
                "abebe@example.com");

        assertTrue(result.isPresent());
        assertEquals("Abebe Kebede", result.get().getName());
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        customerRepository.saveAndFlush(
                new Customer("Sara Ali", "sara@example.com"));

        assertTrue(customerRepository.existsByEmail(
                "sara@example.com"));
    }
}