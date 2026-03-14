package com.homebase.ecom.shared.security;

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
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Shared SecurityFilterChain for all HomeBase microservices.
 *
 * <p>Keycloak realm roles are extracted from the JWT token's
 * {@code realm_access.roles} claim and mapped to Spring Security
 * authorities with a {@code SCOPE_} prefix so that Chenile's
 * {@code STMSecurityStrategy.isAllowed()} can check them against meta-acls.
 */
@Configuration
@EnableWebSecurity
public class HomeBaseSecurityFilterChain {

    private final CorsConfigurationSource corsConfigurationSource;

    public HomeBaseSecurityFilterChain(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public — no token required
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/q/**").permitAll()           // Read-only query endpoints
                .requestMatchers("/webhook/**").permitAll()      // Payment gateway webhooks (Stripe/Razorpay)

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

    /**
     * Extracts Keycloak realm roles from JWT and maps them to SCOPE_-prefixed
     * Spring Security authorities for Chenile meta-acls compatibility.
     *
     * <p>JWT structure: {@code { "realm_access": { "roles": ["CUSTOMER", "ADMIN"] } }}
     * <br>Produces: {@code [SCOPE_CUSTOMER, SCOPE_ADMIN]}
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
