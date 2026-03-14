package com.homebase.ecom.policy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.homebase.ecom.policy", "org.chenile"})
public class PolicyApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolicyApplication.class, args);
    }
}
