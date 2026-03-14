package com.homebase.ecom.checkout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.homebase.ecom.checkout.infrastructure.client")
public class CheckoutServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CheckoutServiceApplication.class, args);
    }
}
