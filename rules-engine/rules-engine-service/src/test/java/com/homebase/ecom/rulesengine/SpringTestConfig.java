package com.homebase.ecom.rulesengine;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@SpringBootApplication(
    scanBasePackages = {
        "org.chenile.**",
        "org.chenile.configuration",
        "com.homebase.ecom.rulesengine.**",
        "org.chenile.service.registry.configuration",
        "org.chenile.pubsub.kafka.configuration"
    }
)
@EnableJpaRepositories(basePackages = {
    "com.homebase.ecom.rulesengine",
    "org.chenile.service.registry.configuration.dao"
})
@EntityScan(basePackages = {
    "com.homebase.ecom.rulesengine",
    "org.chenile.service.registry.model"
})
@ActiveProfiles("unittest")
public class SpringTestConfig extends SpringBootServletInitializer {
}
