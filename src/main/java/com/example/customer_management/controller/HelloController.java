package com.example.customer_management.controller;

import com.example.customer_management.dto.GreetingResponse;
import com.example.customer_management.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/api/hello")
    public String hello() {
        return "hello,from controller";
    }

    @GetMapping("/api/greeting")
    public ResponseEntity<GreetingResponse> greeting() {
        GreetingResponse responses = new GreetingResponse("hello from spring", "customer-mag");

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/api/demo-error")
    public String demoError(){
        throw new ResourceNotFoundException(
                "the requested resource was not found"
        );
    }
}