package com.homebase.ecom.onboarding;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@Configuration
@PropertySource("classpath:com/homebase/ecom/onboarding/TestService.properties")
@SpringBootApplication(scanBasePackages = {
    "org.chenile.configuration",
    "org.chenile.service.registry.configuration",
    "com.homebase.ecom.onboarding.configuration",
    "com.homebase.ecom.cconfig.**"
})
@EnableJpaRepositories(basePackages = {
    "com.homebase.ecom.onboarding",
    "org.chenile.service.registry.configuration.dao",
    "com.homebase.ecom.cconfig.configuration.dao"
})
@EntityScan(basePackages = {
    "com.homebase.ecom.onboarding",
    "org.chenile.service.registry.model",
    "com.homebase.ecom.cconfig.model"
})
@ActiveProfiles("unittest")
public class SpringTestConfig {
}
