package com.homebase.ecom.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = { "com.homebase.ecom",
        "org.chenile","org.chenile.service.registry.configuration","com.homebase.ecom.cconfig.configuration" })
@EntityScan(basePackages = { "com.homebase.ecom", "org.chenile.service.registry.model","com.homebase.ecom.cconfig.model" })
@EnableJpaRepositories(basePackages = { "com.homebase.ecom", "org.chenile.service.registry.configuration.dao","com.homebase.ecom.cconfig.configuration.dao" })
@EnableTransactionManagement
public class QueryBuildApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(QueryBuildApplication.class, args);
    }

}
