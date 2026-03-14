package com.homebase.ecom.supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.homebase.ecom.supplier", "org.chenile"})
public class SupplierApplication {
	public static void main(String[] args) {
		SpringApplication.run(SupplierApplication.class, args);
	}
}
