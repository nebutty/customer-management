package com.example.customer_management.controller;

import com.example.customer_management.dto.CustomerRequest;
import com.example.customer_management.dto.CustomerResponse;
import com.example.customer_management.entity.Customer;
import com.example.customer_management.service.CustomerService;
import com.example.customer_management.dto.CustomerPageResponse;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.customer_management.service.CustomerSearchResult;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.customer_management.dto.CustomerPatchRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<CustomerPageResponse> getAllCustomers(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        CustomerSearchResult result = customerService.searchCustomers(
                name, email, sortBy, direction, page, size
        );

        List<CustomerResponse> content = result.customers()
                .stream()
                .map(this::toResponse)
                .toList();

        int totalPages = (int) (
                (result.totalElements() + size - 1) / size
        );

        CustomerPageResponse response = new CustomerPageResponse(
                content,
                page,
                size,
                result.totalElements(),
                totalPages
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @PathVariable Long id) {

        Customer customer = customerService.findById(id);

        return ResponseEntity.ok(toResponse(customer));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerRequest request) {

        Customer customer = customerService.create(
                request.name(),
                request.email()
        );

        CustomerResponse response = toResponse(customer);

        URI location = URI.create("/api/customers/" + customer.getId());

        return ResponseEntity.created(location).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable long id,
            @RequestBody CustomerRequest request) {

        Customer customer = customerService.update(
                id,
                request.name(),
                request.email()
        );

        CustomerResponse response = toResponse(customer);



        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponse> patchCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerPatchRequest request
    ) {
        Customer updated = customerService.patch(id, request);

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail()
        );
    }
}