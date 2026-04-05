package com.homebase.ecom.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.homebase.ecom", "org.chenile"})
@EntityScan(basePackages = {"com.homebase.ecom", "org.chenile.service.registry.model"})
@EnableJpaRepositories(basePackages = {"com.homebase.ecom", "org.chenile.service.registry.configuration.dao"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
