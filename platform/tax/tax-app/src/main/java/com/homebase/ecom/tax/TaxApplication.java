package com.homebase.ecom.tax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.homebase.ecom.tax", "org.chenile"})
public class TaxApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaxApplication.class, args);
	}
}
