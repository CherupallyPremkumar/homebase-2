package com.homebase.ecom.demoorder;

import org.chenile.stm.STMSecurityStrategy;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test bootstrap for demo-order BDD tests.
 * Excludes Chenile's security configuration to avoid Keycloak dependency.
 */
@Configuration
@SpringBootApplication(scanBasePackages = {
    "org.chenile.configuration",
    "org.chenile.service.registry.configuration",
    "com.homebase.ecom.demoorder.configuration",
    "com.homebase.ecom.demoorder.infrastructure.configuration",
    "com.homebase.ecom.demoorder.invm"
})
@ComponentScan(
    basePackages = {
        "org.chenile.configuration",
        "org.chenile.service.registry.configuration",
        "com.homebase.ecom.demoorder.configuration",
        "com.homebase.ecom.demoorder.infrastructure.configuration",
        "com.homebase.ecom.demoorder.invm"
    },
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "org\\.chenile\\.configuration\\.security\\..*"
    )
)
@EnableJpaRepositories(basePackages = {
    "com.homebase.ecom.demoorder.infrastructure.persistence.repository",
    "org.chenile.service.registry.configuration.dao"
})
@EntityScan(basePackages = {
    "com.homebase.ecom.demoorder.infrastructure.persistence.entity",
    "org.chenile.service.registry.model"
})
@ActiveProfiles("unittest")
public class DemoOrderSpringTestConfig extends SpringBootServletInitializer {

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    public STMSecurityStrategy stmSecurityStrategy() {
        return (String... acls) -> true;
    }
}
