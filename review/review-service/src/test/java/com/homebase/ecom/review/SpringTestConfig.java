package com.homebase.ecom.review;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource("classpath:com/homebase/ecom/review/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "com.homebase.ecom.review.configuration", "org.chenile.service.registry.configuration", "org.chenile.cconfig.**" })
@EnableJpaRepositories(basePackages = {"com.homebase.ecom.review", "org.chenile.service.registry.configuration.dao", "org.chenile.cconfig.configuration.dao"})
@EntityScan(basePackages = {"com.homebase.ecom.review", "org.chenile.service.registry.model", "org.chenile.cconfig.model"})
@ActiveProfiles("unittest")
public class SpringTestConfig {

}
