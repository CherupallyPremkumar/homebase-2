package com.homebase.ecom.shared.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Auto-configuration that activates Keycloak JWT security and CORS
 * whenever spring-security is on the classpath.
 *
 * <p>Drop {@code shared-security} onto any *-app module and security
 * "just works" without any explicit {@code @Configuration} in the app.
 */
@AutoConfiguration
@ConditionalOnClass(HttpSecurity.class)
@Import({HomeBaseSecurityFilterChain.class, HomeBaseCorsConfig.class})
public class HomeBaseSecurityAutoConfiguration {
}
