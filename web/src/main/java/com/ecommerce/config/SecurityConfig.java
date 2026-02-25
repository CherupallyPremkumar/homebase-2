package com.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
                requestHandler.setCsrfRequestAttributeName(null);

                http
                                .csrf(csrf -> csrf
                                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                                .csrfTokenRequestHandler(requestHandler)
                                                .ignoringRequestMatchers("/api/webhooks/**"))
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/api/webhooks/**").permitAll()
                                                .requestMatchers("/products/**").permitAll()
                                                .requestMatchers("/", "/login/**", "/register/**").permitAll()
                                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                                // Cart is accessible without login (guest cart via session)
                                                .requestMatchers("/cart/**").permitAll()
                                                // Checkout and orders require authentication
                                                .requestMatchers("/checkout/**", "/orders/**").authenticated()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/products")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/products")
                                                .permitAll());

                return http.build();
        }

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
                return web -> web.ignoring()
                                .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/.well-known/**");
        }
}
