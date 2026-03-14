package com.homebase.ecom.analytics.client;

import com.homebase.ecom.analytics.service.AnalyticsService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalyticsClientConfiguration {
    @Bean
    public AnalyticsService analyticsServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            AnalyticsService.class,
            "_analyticsService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            null
        );
    }
}
