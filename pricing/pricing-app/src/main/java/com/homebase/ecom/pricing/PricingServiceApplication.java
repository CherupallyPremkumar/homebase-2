package com.homebase.ecom.pricing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.homebase.ecom.pricing")
public class PricingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PricingServiceApplication.class, args);
    }
}
