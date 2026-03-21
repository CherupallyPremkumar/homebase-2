package com.homebase.ecom.support.client;

import com.homebase.ecom.support.service.SupportService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupportClientConfiguration {
    @Bean
    public SupportService supportServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            SupportService.class,
            "_supportStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
