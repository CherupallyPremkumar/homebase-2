package com.homebase.ecom.returnrequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.homebase.ecom.returnrequest", "org.chenile"})
public class ReturnrequestApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReturnrequestApplication.class, args);
	}
}
