package com.homebase.ecom.shipping.client;

import org.chenile.proxy.builder.ProxyBuilder;
import org.chenile.workflow.api.StateEntityService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShippingClientConfiguration {
    @Bean
    @Qualifier("shippingServiceClient")
    public StateEntityService<?> shippingServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            StateEntityService.class,
            "_shippingStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            null
        );
    }
}
