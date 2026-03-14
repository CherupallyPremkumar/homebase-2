package com.homebase.ecom.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.homebase.ecom.inventory", "org.chenile"})
public class InventoryApplication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}
}
