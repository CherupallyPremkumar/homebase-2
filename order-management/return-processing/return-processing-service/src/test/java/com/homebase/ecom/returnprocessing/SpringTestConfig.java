package com.homebase.ecom.returnprocessing;

import org.chenile.stm.STMSecurityStrategy;
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

/**
 * Spring Boot test configuration for Return Processing BDD tests.
 *
 * Excludes Chenile's security configuration to avoid requiring
 * a running Keycloak instance. Provides permissive replacements.
 */
@Configuration
@PropertySource("classpath:com/homebase/ecom/returnprocessing/TestService.properties")
@SpringBootApplication(scanBasePackages = {
    "org.chenile.configuration",
    "org.chenile.service.registry.configuration",
    "com.homebase.ecom.returnprocessing.configuration"
})
@ComponentScan(
    basePackages = {
        "org.chenile.configuration",
        "org.chenile.service.registry.configuration",
        "com.homebase.ecom.returnprocessing.configuration"
    },
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "org\\.chenile\\.configuration\\.security\\..*"
    )
)
@EnableJpaRepositories(basePackages = {
    "com.homebase.ecom.returnprocessing",
    "org.chenile.service.registry.configuration.dao"
})
@EntityScan(basePackages = {
    "com.homebase.ecom.returnprocessing",
    "org.chenile.service.registry.model"
})
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
}
