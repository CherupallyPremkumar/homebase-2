package com.homebase.ecom.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.homebase.ecom.order", "org.chenile"})
public class OrderApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}
}
