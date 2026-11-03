package com.example.customer_management.service;

import com.example.customer_management.entity.Customer;
import java.util.List;

public record CustomerSearchResult(
        List<Customer> customers,
        long totalElements
) {}