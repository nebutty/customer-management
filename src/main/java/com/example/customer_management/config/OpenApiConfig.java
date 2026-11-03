package com.example.customer_management.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Customer Management API",
                version = "1.0.0",
                description = """
                        REST API for managing customers.
                        Supports creating, retrieving, searching, updating,
                        partially updating, and deleting customers.
                        """,
                contact = @Contact(
                        name = "Customer Management Team"
                )
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Enter a valid access token."
)
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer customerApiCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null) {
                return;
            }

            openApi.getPaths().forEach((path, pathItem) ->
                    pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
                        operation.setDescription(
                                operation.getDescription() == null
                                        ? "Endpoint: " + httpMethod.name() + " " + path
                                        : operation.getDescription()
                        );
                    })
            );
        };
    }
}