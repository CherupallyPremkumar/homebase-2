package com.homebase.ecom.query.config;

import org.chenile.core.service.HealthCheckInfo;
import org.chenile.core.service.HealthChecker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryHealthCheckConfiguration {

    @Bean
    public HealthChecker queryHealthChecker() {
        return () -> {
            HealthCheckInfo info = new HealthCheckInfo();
            info.healthy = true;
            info.statusCode = 0;
            info.message = "Query service is healthy";
            return info;
        };
    }
}
