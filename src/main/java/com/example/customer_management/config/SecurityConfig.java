package com.example.customer_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        Converter<Jwt, Collection<GrantedAuthority>> roleConverter =
                new KeycloakRoleConverter();

        JwtAuthenticationConverter jwtConverter =
                new JwtAuthenticationConverter();

        jwtConverter.setJwtGrantedAuthoritiesConverter(roleConverter);

        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers(
                                "/api/hello",
                                "/api/greeting",
                                "/api/demo-error",
                                "/actuator/health/**"
                        ).permitAll()

                        // Swagger and OpenAPI documentation
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml"
                        ).permitAll()

                        // Customer read permissions
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/customers",
                                "/api/customers/**"
                        ).hasAnyRole(
                                "CUSTOMER_USER",
                                "CUSTOMER_ADMIN"
                        )

                        // Customer create permissions
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/customers",
                                "/api/customers/**"
                        ).hasAnyRole(
                                "CUSTOMER_USER",
                                "CUSTOMER_ADMIN"
                        )

                        // Customer delete permissions
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/customers/**"
                        ).hasRole("CUSTOMER_ADMIN")

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtConverter)
                        )
                );

        return http.build();
    }
}