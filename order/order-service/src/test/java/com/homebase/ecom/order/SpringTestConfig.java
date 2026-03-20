package com.homebase.ecom.order;

import org.chenile.stm.STMSecurityStrategy;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import com.homebase.ecom.order.service.event.OrderEventPublisher;

/**
 * Spring Boot test configuration for Order BDD tests.
 *
 * Excludes Chenile's security configuration (ChenileSecurityConfiguration +
 * SecurityApiConfiguration + ConnectionConfiguration) to avoid requiring
 * a running Keycloak instance. Provides stub replacements.
 */
@Configuration
@PropertySource("classpath:com/homebase/ecom/order/TestService.properties")
@SpringBootApplication(scanBasePackages = {
    "org.chenile.configuration",
    "org.chenile.service.registry.configuration",
    "com.homebase.ecom.order.configuration",
    "com.homebase.ecom.order.infrastructure"
})
@ComponentScan(
    basePackages = {
        "org.chenile.configuration",
        "org.chenile.service.registry.configuration",
        "com.homebase.ecom.order.configuration",
        "com.homebase.ecom.order.infrastructure"
    },
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "org\\.chenile\\.configuration\\.security\\..*"
    )
)
@EnableJpaRepositories(basePackages = { "com.homebase.ecom.order", "org.chenile.service.registry.configuration.dao" })
@EntityScan(basePackages = { "com.homebase.ecom.order", "org.chenile.service.registry.model" })
@ActiveProfiles("unittest")
public class SpringTestConfig {

    /**
     * Permissive security filter chain for unit tests.
     */
    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    /**
     * Permissive STM security strategy -- allows all transitions in unit tests.
     * Replaces the real StmSecurityStrategyImpl that requires Keycloak.
     */
    @Bean
    public STMSecurityStrategy stmSecurityStrategy() {
        return (String... acls) -> true;
    }

    @Bean
    public OrderEventPublisher orderEventPublisher() {
        return Mockito.mock(OrderEventPublisher.class);
    }
}
