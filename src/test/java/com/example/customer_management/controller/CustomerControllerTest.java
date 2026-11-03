package com.example.customer_management.controller;

import com.example.customer_management.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = CustomerController.class,
        excludeAutoConfiguration =
                org.springframework.boot.security.oauth2.server.resource.autoconfigure.web
                        .OAuth2ResourceServerWebSecurityAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void createCustomer_shouldReturnCreatedCustomer() throws Exception {
        var customer = org.mockito.Mockito.mock(
                com.example.customer_management.entity.Customer.class
        );

        when(customer.getId()).thenReturn(1L);
        when(customer.getName()).thenReturn("Abebe Kebede");
        when(customer.getEmail()).thenReturn("abebe@example.com");

        when(customerService.create(anyString(), anyString()))
                .thenReturn(customer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Abebe Kebede",
                                    "email": "abebe@example.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/customers/1"))
                .andExpect(jsonPath("$.name").value("Abebe Kebede"))
                .andExpect(jsonPath("$.email").value("abebe@example.com"));
    }}

