package com.homebase.ecom.tax;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@PropertySource("classpath:com/homebase/ecom/tax/TestService.properties")
@SpringBootApplication(scanBasePackages = {
    "org.chenile.configuration",
    "org.chenile.service.registry.configuration",
    "com.homebase.ecom.tax.controller",
    "com.homebase.ecom.tax.configuration",
    "com.homebase.ecom.tax.infrastructure",
    "com.homebase.ecom.tax.bdd"
})
@EnableJpaRepositories(basePackages = {
    "org.chenile.service.registry.configuration.dao",
    "com.homebase.ecom.tax.infrastructure.persistence.adapter"
})
@EntityScan(basePackages = {
    "org.chenile.service.registry.model",
    "com.homebase.ecom.tax.infrastructure.persistence.entity"
})
@ActiveProfiles("unittest")
public class SpringTestConfig extends SpringBootServletInitializer {
}
