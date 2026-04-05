package com.homebase.ecom.payment.client;

import com.homebase.ecom.payment.gateway.service.PaymentService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentClientConfiguration {
    @Bean
    public PaymentService paymentServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            PaymentService.class,
            "_paymentStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
