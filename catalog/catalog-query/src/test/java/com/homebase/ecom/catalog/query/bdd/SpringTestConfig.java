package com.homebase.ecom.catalog.query.bdd;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Configuration
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "com.homebase.ecom.query.service.configuration"})
@ActiveProfiles("unittest")
@Testcontainers
public class SpringTestConfig {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("catalog_test")
            .withUsername("test")
            .withPassword("test")
            .withCommand("postgres", "-c", "max_connections=50");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        // Chenile query datasource (tenant-based)
        registry.add("query.datasources.homebase.jdbcUrl", postgres::getJdbcUrl);
        registry.add("query.datasources.homebase.username", postgres::getUsername);
        registry.add("query.datasources.homebase.password", postgres::getPassword);
    }
}
