package com.homebase.ecom.returnrequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.homebase.ecom", "org.chenile"})
@EntityScan(basePackages = {"com.homebase.ecom", "org.chenile.service.registry.model"})
@EnableJpaRepositories(basePackages = {"com.homebase.ecom", "org.chenile.service.registry.configuration.dao"})
public class ReturnrequestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReturnrequestApplication.class, args);
    }
}
