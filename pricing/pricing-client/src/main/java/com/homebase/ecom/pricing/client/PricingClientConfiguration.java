package com.homebase.ecom.pricing.client;

import com.homebase.ecom.pricing.api.service.PricingService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PricingClientConfiguration {
    @Bean
    public PricingService pricingServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            PricingService.class,
            "_pricingService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            null
        );
    }
}
