package com.homebase.ecom.demoorder.client;

import com.homebase.ecom.demoorder.service.DemoOrderService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Demo-order client configuration following the Chenile proxy pattern.
 * Creates a proxy bean for cross-BC consumption of the demo-order service.
 */
@Configuration
public class DemoOrderClientConfiguration {

    @Bean
    public DemoOrderService demoOrderServiceProxy(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            DemoOrderService.class,
            "_demoOrderStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
