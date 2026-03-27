package com.homebase.ecom.build.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Security configuration that integrates Keycloak JWT with Chenile ACLs.
 *
 * Keycloak realm roles are extracted from the JWT token's realm_access.roles claim
 * and mapped to Spring Security authorities with SCOPE_ prefix so that Chenile's
 * STMSecurityStrategy.isAllowed() can check them against meta-acls.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${homebase.cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public — no token required
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/webhook/**").permitAll()      // Payment gateway webhooks (Stripe/Razorpay)
                .requestMatchers("GET", "/product/**").permitAll()
                .requestMatchers("GET", "/catalog/**").permitAll()
                .requestMatchers("GET", "/q/**").permitAll()    // Read-only query endpoints

                // Admin panel
                .requestMatchers("/admin/**").hasAuthority("SCOPE_ADMIN")

                // Inventory & warehouse operations
                .requestMatchers("/inventory/**").hasAnyAuthority("SCOPE_WAREHOUSE", "SCOPE_ADMIN", "SCOPE_SYSTEM")

                // Order management — customers create, warehouse/admin manage
                .requestMatchers("POST", "/order/**").hasAnyAuthority("SCOPE_CUSTOMER", "SCOPE_SYSTEM")
                .requestMatchers("PATCH", "/order/**").hasAnyAuthority("SCOPE_CUSTOMER", "SCOPE_WAREHOUSE", "SCOPE_ADMIN", "SCOPE_SYSTEM")

                // Cart — customers only
                .requestMatchers("/cart/**").hasAnyAuthority("SCOPE_CUSTOMER", "SCOPE_SYSTEM")

                // Supplier onboarding
                .requestMatchers("/onboarding/**").hasAnyAuthority("SCOPE_SUPPLIER", "SCOPE_ADMIN", "SCOPE_SYSTEM")
                .requestMatchers("/supplier/**").hasAnyAuthority("SCOPE_SUPPLIER", "SCOPE_ADMIN", "SCOPE_SYSTEM")

                // Returns & refunds
                .requestMatchers("/returnrequest/**").hasAnyAuthority("SCOPE_CUSTOMER", "SCOPE_QUALITY_CHECKER", "SCOPE_ADMIN", "SCOPE_SYSTEM")

                // Support tickets
                .requestMatchers("/support/**").hasAnyAuthority("SCOPE_CUSTOMER", "SCOPE_AGENT", "SCOPE_ADMIN")

                // Reviews — customers submit, admins moderate
                .requestMatchers("/review/**").hasAnyAuthority("SCOPE_CUSTOMER", "SCOPE_ADMIN", "SCOPE_SYSTEM")

                // Product management — admins only for writes
                .requestMatchers("POST", "/product/**").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_SUPPLIER")
                .requestMatchers("PATCH", "/product/**").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_SUPPLIER", "SCOPE_SYSTEM")

                // Settlement — admin confirms payouts
                .requestMatchers("/settlement/**").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_SYSTEM")

                // Everything else requires authentication
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );
        return http.build();
    }

    /**
     * Converts Keycloak realm_access.roles into SCOPE_-prefixed authorities
     * for Chenile ACL compatibility.
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return converter;
    }

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

    /**
     * Extracts Keycloak realm roles from JWT and maps them to SCOPE_-prefixed
     * Spring Security authorities for Chenile meta-acls compatibility.
     *
     * JWT structure: { "realm_access": { "roles": ["CUSTOMER", "ADMIN"] } }
     * Produces: [SCOPE_CUSTOMER, SCOPE_ADMIN]
     */
    static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        @SuppressWarnings("unchecked")
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            List<GrantedAuthority> authorities = new ArrayList<>();

            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess != null) {
                Object rolesObj = realmAccess.get("roles");
                if (rolesObj instanceof List<?> roles) {
                    for (Object role : roles) {
                        // SCOPE_ prefix matches Chenile STMSecurityStrategy expectations
                        authorities.add(new SimpleGrantedAuthority("SCOPE_" + role));
                    }
                }
            }
            return authorities;
        }
    }
}
