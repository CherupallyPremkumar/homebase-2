package com.homebase.ecom.rulesengine.query;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@SpringBootApplication(scanBasePackages = {
    "org.chenile.configuration",
    "com.homebase.ecom.rulesengine",
    "com.homebase.ecom.query.service"
})
@EnableJpaRepositories(basePackages = {
    "com.homebase.ecom.rulesengine",
    "org.chenile.service.registry.configuration.dao"
})
@EntityScan(basePackages = {
    "com.homebase.ecom.rulesengine",
    "org.chenile.service.registry.model"
})
@ActiveProfiles("unittest")
public class SpringTestConfig {
}
