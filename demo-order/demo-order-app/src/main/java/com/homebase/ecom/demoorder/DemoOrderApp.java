package com.homebase.ecom.demoorder;

import org.chenile.security.service.SecurityService;
import org.chenile.service.registry.service.ServiceRegistryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Standalone Spring Boot application for demo-order.
 * Runs on port 8080 as an independent JVM.
 * Uses Kafka for publishing events to demo-notification.
 */
@SpringBootApplication(
    scanBasePackages = {
        "org.chenile.configuration",
        "org.chenile.core",
        "org.chenile.http",
        "org.chenile.stm",
        "org.chenile.workflow",
        "org.chenile.pubsub",
        "org.chenile.service.registry.configuration",
        "com.homebase.ecom.demoorder"
    }
)
@EnableJpaRepositories(basePackages = {
    "com.homebase.ecom.demoorder.infrastructure.persistence.repository",
    "org.chenile.service.registry.configuration.dao"
})
@EntityScan(basePackages = {
    "com.homebase.ecom.demoorder.infrastructure.persistence.entity",
    "org.chenile.service.registry.model"
})
public class DemoOrderApp {
    public static void main(String[] args) {
        SpringApplication.run(DemoOrderApp.class, args);
    }

    @Bean
    @Primary
    ServiceRegistryService primaryServiceRegistry(
            @Qualifier("_serviceregistryService_") ServiceRegistryService srs) {
        return srs;
    }

    @Bean
    SecurityService securityService() {
        return new SecurityService() {
            @Override public String[] getCurrentAuthorities() { return new String[]{"*"}; }
            @Override public boolean doesCurrentUserHaveGuardingAuthorities(org.chenile.core.context.ChenileExchange e) { return true; }
            @Override public boolean doesCurrentUserHaveGuardingAuthorities(String... a) { return true; }
        };
    }
}
