package com.homebase.ecom.cart;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test bootstrap for cart-service BDD tests.
 * Port stubs come from CartInfrastructureTestConfiguration (cart-infrastructure test-jar).
 */
@Configuration
@SpringBootApplication(
    scanBasePackages = {
        "org.chenile.**",
        "org.chenile.configuration",
        "com.homebase.ecom.cart.**",
        "org.chenile.service.registry.configuration",
        "org.chenile.pubsub.kafka.configuration"
    }
)
@EnableJpaRepositories(basePackages = { "com.homebase.ecom.cart", "org.chenile.service.registry.configuration.dao", "com.homebase.ecom.cconfig.configuration.dao" })
@EntityScan(basePackages = { "com.homebase.ecom.cart", "org.chenile.service.registry.model", "com.homebase.ecom.cconfig.model" })
@ActiveProfiles("unittest")
public class SpringTestConfig extends SpringBootServletInitializer {
    // All test port beans provided by CartInfrastructureTestConfiguration
    // via cart-infrastructure test-jar dependency
}
