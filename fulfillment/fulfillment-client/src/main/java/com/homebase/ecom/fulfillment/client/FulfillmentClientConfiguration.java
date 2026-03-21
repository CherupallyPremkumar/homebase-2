package com.homebase.ecom.fulfillment.client;

import com.homebase.ecom.fulfillment.service.FulfillmentService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FulfillmentClientConfiguration {
    @Bean
    public FulfillmentService fulfillmentServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            FulfillmentService.class,
            "_fulfillmentStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
