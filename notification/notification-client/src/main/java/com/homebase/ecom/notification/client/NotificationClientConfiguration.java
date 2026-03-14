package com.homebase.ecom.notification.client;

import com.homebase.ecom.notification.service.NotificationService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationClientConfiguration {
    @Bean
    public NotificationService notificationServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            NotificationService.class,
            "_notificationStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            null
        );
    }
}
