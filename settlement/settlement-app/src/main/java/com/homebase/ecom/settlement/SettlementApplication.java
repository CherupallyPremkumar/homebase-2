package com.homebase.ecom.settlement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.homebase.ecom.settlement", "org.chenile"})
public class SettlementApplication {
	public static void main(String[] args) {
		SpringApplication.run(SettlementApplication.class, args);
	}
}
