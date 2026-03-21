package com.homebase.ecom.demonotification.client;

import com.homebase.ecom.demonotification.service.DemoNotificationService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Demo-notification client configuration following the Chenile proxy pattern.
 * Creates a proxy bean for cross-BC consumption of the demo-notification service.
 */
@Configuration
public class DemoNotificationClientConfiguration {

    @Bean
    public DemoNotificationService demoNotifServiceProxy(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            DemoNotificationService.class,
            "_demoNotifStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8081"
        );
    }
}
