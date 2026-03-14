package com.homebase.ecom.offer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.homebase.ecom.offer", "org.chenile"})
public class OfferApplication {
    public static void main(String[] args) {
        SpringApplication.run(OfferApplication.class, args);
    }
}
