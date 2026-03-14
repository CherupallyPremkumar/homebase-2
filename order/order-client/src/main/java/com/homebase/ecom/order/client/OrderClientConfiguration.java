package com.homebase.ecom.order.client;

import com.homebase.ecom.order.service.OrderService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderClientConfiguration {
    @Bean
    public OrderService orderServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            OrderService.class,
            "_orderStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            null
        );
    }
}
