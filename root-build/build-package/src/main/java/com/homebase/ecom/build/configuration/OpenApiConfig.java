package com.homebase.ecom.build.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger UI configuration for the HomeBase E-Commerce Platform.
 *
 * Swagger UI is available at /swagger-ui.html
 * OpenAPI spec is available at /api-docs
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "HomeBase E-Commerce Platform API",
        version = "0.0.1-SNAPSHOT",
        description = "REST API for the HomeBase multi-vendor e-commerce platform. "
            + "All state-entity endpoints follow the Chenile STM pattern: "
            + "POST to create, GET to retrieve, PATCH /{id}/{eventID} to trigger state transitions.",
        contact = @Contact(name = "HomeBase Team")
    ),
    security = @SecurityRequirement(name = "bearer-jwt")
)
@SecurityScheme(
    name = "bearer-jwt",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Keycloak-issued JWT token. Obtain from POST /realms/homebase/protocol/openid-connect/token"
)
public class OpenApiConfig {
}
