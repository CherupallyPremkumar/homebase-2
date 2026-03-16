package com.homebase.ecom.shipping;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.shipping.model.Shipping;


@Configuration
@PropertySource("classpath:com/homebase/ecom/shipping/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "org.chenile.service.registry.configuration", "com.homebase.ecom.shipping.configuration" })
@EnableJpaRepositories(basePackages = { "com.homebase.ecom.shipping", "org.chenile.service.registry.configuration.dao" })
@EntityScan(basePackages = { "com.homebase.ecom.shipping", "org.chenile.service.registry.model" })
@ActiveProfiles("unittest")
public class SpringTestConfig{
	
}

