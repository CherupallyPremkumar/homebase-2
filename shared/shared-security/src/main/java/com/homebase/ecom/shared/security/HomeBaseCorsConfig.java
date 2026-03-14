package com.homebase.ecom.shared.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * CORS configuration for HomeBase services.
 *
 * <p>Allowed origins are configurable via the property
 * {@code homebase.security.cors.allowed-origins}. Defaults to
 * {@code http://localhost:4200} and {@code http://localhost:8080}.
 */
@Configuration
public class HomeBaseCorsConfig {

    @Value("${homebase.security.cors.allowed-origins:http://localhost:4200,http://localhost:8080}")
    private List<String> allowedOrigins;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
