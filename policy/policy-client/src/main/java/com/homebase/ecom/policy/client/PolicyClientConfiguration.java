package com.homebase.ecom.policy.client;

import com.homebase.ecom.policy.api.service.DecisionService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PolicyClientConfiguration {
    @Bean
    public DecisionService decisionServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            DecisionService.class,
            "decisionService",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            null
        );
    }
}
