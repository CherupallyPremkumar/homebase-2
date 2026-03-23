package com.homebase.ecom.query.config;

import com.homebase.ecom.query.interceptor.CustomerFilterInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Security-related beans for query-build.
 * Registers interceptors that enforce server-side filtering from JWT claims.
 */
@Configuration
public class QuerySecurityConfiguration {

    @Bean
    CustomerFilterInterceptor customerFilterInterceptor() {
        return new CustomerFilterInterceptor();
    }
}
