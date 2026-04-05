package com.homebase.ecom.query;

import org.chenile.service.registry.service.ServiceRegistryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Standalone query build application.
 * Security: QueryResourceServerSecurityConfig permits all at Spring level,
 * Chenile ACL interceptor enforces per-query auth using JWT when present.
 */
@SpringBootApplication(
    exclude = {
        OAuth2ClientAutoConfiguration.class,
    },
    scanBasePackages = {
        "org.chenile.configuration.core",
        "org.chenile.configuration.http",
        "org.chenile.configuration.query",
        "org.chenile.configuration.controller",
        "org.chenile.core",
        "org.chenile.http",
        "org.chenile.query",
        "org.chenile.stm",
        "org.chenile.workflow",
        "org.chenile.service.registry.configuration",
        "com.homebase.ecom.query",
        "com.homebase.ecom.shared",
    }
)
@EnableJpaRepositories(basePackages = {
    "org.chenile.service.registry.configuration.dao"
})
@EntityScan(basePackages = {
    "org.chenile.service.registry.model"
})
@EnableTransactionManagement
public class QueryBuildApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(QueryBuildApplication.class, args);
    }

    @Bean
    @Primary
    ServiceRegistryService primaryServiceRegistry(
            @Qualifier("_serviceregistryService_") ServiceRegistryService srs) {
        return srs;
    }
}
