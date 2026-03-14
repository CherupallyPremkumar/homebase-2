package com.homebase.ecom.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.homebase.ecom.support", "org.chenile"})
public class SupportApplication {
	public static void main(String[] args) {
		SpringApplication.run(SupportApplication.class, args);
	}
}
