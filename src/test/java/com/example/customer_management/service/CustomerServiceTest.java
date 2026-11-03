
package com.example.customer_management.service;

import com.example.customer_management.entity.Customer;
import com.example.customer_management.exception.ResourceNotFoundException;
import com.example.customer_management.respository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("Abebe Kebede", "abebe@example.com");
    }

    // CREATE TESTS

    @Test
    void create_shouldSaveCustomer_whenEmailDoesNotExist() {
        when(customerRepository.existsByEmail("abebe@example.com"))
                .thenReturn(false);

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(customer);

        Customer result = customerService.create(
                "Abebe Kebede",
                "abebe@example.com"
        );

        assertNotNull(result);
        assertEquals("Abebe Kebede", result.getName());
        assertEquals("abebe@example.com", result.getEmail());

        verify(customerRepository).existsByEmail("abebe@example.com");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void create_shouldThrowException_whenEmailAlreadyExists() {
        when(customerRepository.existsByEmail("abebe@example.com"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.create(
                        "Abebe Kebede",
                        "abebe@example.com"
                )
        );

        assertEquals(
                "A customer with this email already exists",
                exception.getMessage()
        );

        verify(customerRepository, never()).save(any(Customer.class));
    }

    // FIND BY ID TESTS

    @Test
    void findById_shouldReturnCustomer_whenIdExists() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        Customer result = customerService.findById(1L);

        assertSame(customer, result);
        verify(customerRepository).findById(1L);
    }

    @Test
    void findById_shouldThrowException_whenIdDoesNotExist() {
        when(customerRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> customerService.findById(99L)
        );

        verify(customerRepository).findById(99L);
    }

    @Test
    void findById_shouldThrowException_whenIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> customerService.findById(null)
        );

        verifyNoInteractions(customerRepository);
    }

    @Test
    void findById_shouldThrowException_whenIdIsZeroOrNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> customerService.findById(0L)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> customerService.findById(-1L)
        );

        verifyNoInteractions(customerRepository);
    }
}
