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
    "com.homebase.ecom.onboarding.configuration",
    "com.homebase.ecom.supplier.configuration",
    "org.chenile.cconfig.**"
})
@EnableJpaRepositories(basePackages = {
    "com.homebase.ecom.onboarding",
    "com.homebase.ecom.supplier",
    "org.chenile.cconfig.configuration.dao"
})
@EntityScan(basePackages = {
    "com.homebase.ecom.onboarding",
    "com.homebase.ecom.supplier",
    "org.chenile.cconfig.model"
})
@ActiveProfiles("unittest")
public class SpringTestConfig {
}
