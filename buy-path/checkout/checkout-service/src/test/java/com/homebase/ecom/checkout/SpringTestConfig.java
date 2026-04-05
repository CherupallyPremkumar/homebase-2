package com.homebase.ecom.checkout;

import com.homebase.ecom.checkout.infrastructure.test.CheckoutInfrastructureTestConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@PropertySource("classpath:com/homebase/ecom/checkout/TestService.properties")
@SpringBootApplication(scanBasePackages = {
    "org.chenile.configuration",
    "org.chenile.service.registry.configuration",
    "com.homebase.ecom.checkout.configuration",
    "com.homebase.ecom.checkout.infrastructure.persistence",
    "com.homebase.ecom.checkout.infrastructure.persistence.mapper",
    "com.homebase.ecom.checkout.bdd",
    "org.chenile.configuration.security"
})
@EnableJpaRepositories(basePackages = { "com.homebase.ecom.checkout", "org.chenile.service.registry.configuration.dao" })
@EntityScan(basePackages = { "com.homebase.ecom.checkout", "org.chenile.service.registry.model" })
@ActiveProfiles("unittest")
@Import(CheckoutInfrastructureTestConfiguration.class)
public class SpringTestConfig extends SpringBootServletInitializer {
    // All test stub port beans are provided by CheckoutInfrastructureTestConfiguration
    // imported from checkout-infrastructure test-jar.
}
