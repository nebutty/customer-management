package com.example.customer_management.dto;

import java.util.Map;

public record ApiErrorResponse(
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors
) {}