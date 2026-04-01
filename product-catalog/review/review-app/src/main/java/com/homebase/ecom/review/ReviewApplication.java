package com.homebase.ecom.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.homebase.ecom.review", "org.chenile"})
public class ReviewApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReviewApplication.class, args);
	}
}
