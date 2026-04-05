package com.homebase.ecom.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.homebase.ecom", "org.chenile"})
@EntityScan(basePackages = {"com.homebase.ecom", "org.chenile.service.registry.model"})
@EnableJpaRepositories(basePackages = {"com.homebase.ecom", "org.chenile.service.registry.configuration.dao"})
public class PaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
}
