package com.homebase.ecom.notification;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@PropertySource("classpath:com/homebase/ecom/notification/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "org.chenile.service.registry.configuration", "com.homebase.ecom.notification.configuration", "com.homebase.ecom.notification.infrastructure" })
@EnableJpaRepositories(basePackages = {"com.homebase.ecom.notification", "org.chenile.service.registry.configuration.dao"})
@EntityScan(basePackages = {"com.homebase.ecom.notification", "org.chenile.service.registry.model"})
@ActiveProfiles("unittest")
public class SpringTestConfig {
}
