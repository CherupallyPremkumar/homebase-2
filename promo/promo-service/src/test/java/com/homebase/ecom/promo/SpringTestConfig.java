package com.homebase.ecom.promo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource("classpath:com/homebase/ecom/promo/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "org.chenile.service.registry.configuration", "com.homebase.ecom.promo.configuration", "org.chenile.cconfig.**" })
@EnableJpaRepositories(basePackages = {"com.homebase.ecom.promo", "org.chenile.cconfig.configuration", "org.chenile.service.registry.configuration.dao"})
@EntityScan(basePackages = {"com.homebase.ecom.promo", "org.chenile.service.registry.model", "org.chenile.cconfig.model"})
@ActiveProfiles("unittest")
public class SpringTestConfig {

}
