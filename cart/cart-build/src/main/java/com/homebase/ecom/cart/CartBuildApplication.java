package com.homebase.ecom.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * Standalone application to build and run the Cart service.
 * Aggregates all cart-related components (API, implementation, queries, events).
 */
@SpringBootApplication(scanBasePackages = {"com.homebase.ecom.cart", "org.chenile"})
public class CartBuildApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartBuildApplication.class, args);
    }
}
