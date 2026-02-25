package com.homebase.ecom.build;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = { "com.homebase.ecom.**.configuration",
		"org.chenile.configuration" })
@EntityScan("com.homebase.ecom.**.model")
@EnableJpaRepositories(basePackages = "com.homebase.ecom.**.configuration.dao")
@EnableTransactionManagement
public class BuildApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BuildApplication.class, args);
	}
}
