package com.homebase.ecom.settlement.client;

import org.chenile.proxy.builder.ProxyBuilder;
import org.chenile.workflow.api.StateEntityService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SettlementClientConfiguration {
    @Bean
    @Qualifier("settlementServiceClient")
    public StateEntityService<?> settlementServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            StateEntityService.class,
            "_settlementStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
