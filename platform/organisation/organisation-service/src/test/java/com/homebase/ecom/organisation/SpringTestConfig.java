package com.homebase.ecom.organisation;

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
        "com.homebase.ecom.organisation.**",
        "org.chenile.service.registry.configuration"
    }
)
@EnableJpaRepositories(basePackages = { "com.homebase.ecom.organisation", "org.chenile.service.registry.configuration.dao" })
@EntityScan(basePackages = { "com.homebase.ecom.organisation", "org.chenile.service.registry.model" })
@ActiveProfiles("unittest")
public class SpringTestConfig extends SpringBootServletInitializer {
}
