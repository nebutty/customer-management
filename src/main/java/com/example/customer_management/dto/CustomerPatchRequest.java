package com.example.customer_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CustomerPatchRequest(

        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @Email(message = "Please provide a valid email address")
        @Size(max = 254, message = "Email must not exceed 254 characters")
        String email

) {}