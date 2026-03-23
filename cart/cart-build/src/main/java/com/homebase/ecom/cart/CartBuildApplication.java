package com.homebase.ecom.cart;

import com.homebase.ecom.cart.jobs.query.CartQueryPort;
import com.homebase.ecom.cart.service.handler.CartExternalEventHandler;
import org.chenile.service.registry.service.ServiceRegistryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Standalone application to run the Cart service independently.
 * Connects to PostgreSQL, Kafka, and Keycloak from docker-compose.
 */
@SpringBootApplication(scanBasePackages = {
    "org.chenile.configuration",
    "org.chenile.core",
    "org.chenile.http",
    "org.chenile.stm",
    "org.chenile.workflow",
    "org.chenile.pubsub",
    "org.chenile.service.registry.configuration",
    "org.chenile.security",
    "com.homebase.ecom.cart",
    "com.homebase.ecom.shared",
    "com.homebase.ecom.pricing.client",
    "com.homebase.ecom.inventory.client",
    "com.homebase.ecom.product.client",
    "com.homebase.ecom.cconfig"
})
@EnableJpaRepositories(basePackages = {
    "com.homebase.ecom.cart",
    "org.chenile.service.registry.configuration.dao",
    "com.homebase.ecom.cconfig.configuration.dao"
})
@EntityScan(basePackages = {
    "com.homebase.ecom.cart",
    "org.chenile.service.registry.model",
    "com.homebase.ecom.cconfig.model"
})
public class CartBuildApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartBuildApplication.class, args);
    }

    @Bean
    CartExternalEventHandler.CartQueryPort cartExternalEventQueryPort() {
        return new CartExternalEventHandler.CartQueryPort() {
            @Override public java.util.List<String> findActiveCartsWithVariant(String variantId) { return java.util.List.of(); }
            @Override public java.util.List<String> findActiveCartsWithCoupon(String couponCode) { return java.util.List.of(); }
        };
    }

    @Bean
    CartQueryPort cartJobsQueryPort() {
        return new CartQueryPort() {
            @Override public java.util.List<String> findActiveCartsWithVariant(String variantId) { return java.util.List.of(); }
            @Override public java.util.List<String> findActiveCartsWithCoupon(String couponCode) { return java.util.List.of(); }
            @Override public java.util.List<String> findExpiredCarts() { return java.util.List.of(); }
            @Override public java.util.List<String> findIdleCarts(int thresholdHours) { return java.util.List.of(); }
        };
    }

    @Bean
    @Primary
    ServiceRegistryService primaryServiceRegistry(
            @Qualifier("_serviceregistryService_") ServiceRegistryService srs) {
        return srs;
    }
}
