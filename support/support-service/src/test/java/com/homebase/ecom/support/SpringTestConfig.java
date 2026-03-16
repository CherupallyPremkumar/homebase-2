package com.homebase.ecom.support;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@PropertySource("classpath:com/homebase/ecom/support/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "org.chenile.service.registry.configuration", "com.homebase.ecom.support.configuration", "com.homebase.ecom.support.infrastructure" })
@EnableJpaRepositories(basePackages = {"com.homebase.ecom.support", "org.chenile.service.registry.configuration.dao"})
@EntityScan(basePackages = {"com.homebase.ecom.support", "org.chenile.service.registry.model"})
@ActiveProfiles("unittest")
public class SpringTestConfig {
}
