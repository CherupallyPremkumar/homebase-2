package com.homebase.ecom.payment;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@PropertySource("classpath:com/homebase/ecom/payment/TestService.properties")
@SpringBootApplication(scanBasePackages = {
        "org.chenile",
        "com.homebase.ecom.payment.configuration"
})
@ComponentScan(basePackages = {
        "org.chenile",
        "com.homebase.ecom.payment.configuration"
}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.homebase\\.ecom\\.payment\\.service\\..*")
})
@EnableJpaRepositories(basePackages = { "com.homebase.ecom.payment.infrastructure", "org.chenile.service.registry.configuration.dao" })
@EntityScan(basePackages = { "com.homebase.ecom.payment.infrastructure", "org.chenile.service.registry.model" })
@ActiveProfiles("unittest")
public class SpringTestConfig {
}
