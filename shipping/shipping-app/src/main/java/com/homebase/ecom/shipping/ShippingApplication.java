package com.homebase.ecom.shipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.homebase.ecom.shipping", "org.chenile"})
public class ShippingApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShippingApplication.class, args);
	}
}
