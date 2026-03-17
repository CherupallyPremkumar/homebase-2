package com.homebase.ecom.returnprocessing;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource("classpath:com/homebase/ecom/returnprocessing/TestService-chenile.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration",
        "com.homebase.ecom.returnprocessing.configuration",
        "org.chenile.service.registry.configuration",
        "com.homebase.ecom.cconfig.**" })
@EnableJpaRepositories(basePackages = {"com.homebase.ecom.returnprocessing",
        "org.chenile.service.registry.configuration.dao",
        "com.homebase.ecom.cconfig.configuration.dao"})
@EntityScan(basePackages = {"com.homebase.ecom.returnprocessing",
        "org.chenile.service.registry.model",
        "com.homebase.ecom.cconfig.model"})
@ActiveProfiles("unittest")
public class SpringTestConfig {
}
