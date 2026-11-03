
package com.example.customer_management.service;

import com.example.customer_management.dto.CustomerPatchRequest;
import com.example.customer_management.entity.Customer;
import com.example.customer_management.exception.ResourceNotFoundException;

import com.example.customer_management.respository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Long customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException(
                    "Customer ID must be a positive number"
            );
        }

        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with ID: " + customerId
                ));
    }

    public Customer create(String name, String email) {
        if(customerRepository.existsByEmail(email)){
            throw new IllegalArgumentException("A customer with this email already exists");
        }
        Customer customer = new Customer(name, email);

        return customerRepository.save(customer);
    }

    public Customer update(Long customerId, String name, String email) {
        Customer existing = findById(customerId);

        existing.setName(name);
        existing.setEmail(email);

        return customerRepository.save(existing);
    }

    public CustomerSearchResult searchCustomers(
            String name,
            String email,
            String sortBy,
            String direction,
            int page,
            int size
    ) {
        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page must be zero or greater"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Size must be between 1 and 100"
            );
        }

        Comparator<Customer> comparator = switch (sortBy) {
            case "id" -> Comparator.comparing(Customer::getId);
            case "name" -> Comparator.comparing(
                    Customer::getName,
                    String.CASE_INSENSITIVE_ORDER
            );
            case "email" -> Comparator.comparing(
                    Customer::getEmail,
                    String.CASE_INSENSITIVE_ORDER
            );
            default -> throw new IllegalArgumentException(
                    "sortBy must be id, name, or email"
            );
        };

        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        } else if (!"asc".equalsIgnoreCase(direction)) {
            throw new IllegalArgumentException(
                    "direction must be asc or desc"
            );
        }

        String nameFilter = name == null
                ? "" : name.trim().toLowerCase(Locale.ROOT);

        String emailFilter = email == null
                ? "" : email.trim().toLowerCase(Locale.ROOT);

        List<Customer> filtered = customerRepository.findAll()
                .stream()
                .filter(c -> c.getName()
                        .toLowerCase(Locale.ROOT)
                        .contains(nameFilter))
                .filter(c -> c.getEmail()
                        .toLowerCase(Locale.ROOT)
                        .contains(emailFilter))
                .sorted(comparator)
                .toList();

        long totalElements = filtered.size();

        int start = (int) Math.min(
                (long) page * size, totalElements
        );

        int end = (int) Math.min(
                (long) start + size, totalElements
        );

        List<Customer> pageCustomers =
                filtered.subList(start, end);

        return new CustomerSearchResult(
                pageCustomers, totalElements
        );
    }

    public Customer patch(Long id, CustomerPatchRequest request) {
        Customer existing = findById(id);

        if (request.name() == null && request.email() == null) {
            throw new IllegalArgumentException(
                    "At least one field must be provided for update"
            );
        }

        String name = request.name() != null
                ? request.name().trim()
                : existing.getName();

        String email = request.email() != null
                ? request.email().trim()
                : existing.getEmail();

        if (name.isBlank()) {
            throw new IllegalArgumentException(
                    "Name cannot be blank"
            );
        }

        if (email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email cannot be blank"
            );
        }

        existing.setName(name);
        existing.setEmail(email);

        return customerRepository.save(existing);
    }

    public void delete(Long id) {
        Customer existing = findById(id);
        customerRepository.delete(existing);
    }
}
