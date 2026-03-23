package com.homebase.ecom.build.bdd;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.homebase.ecom", "org.chenile"})
@EntityScan(basePackages = {"com.homebase.ecom", "org.chenile"})
@EnableJpaRepositories(basePackages = {"com.homebase.ecom", "org.chenile"})
public class SpringTestConfig {
}
