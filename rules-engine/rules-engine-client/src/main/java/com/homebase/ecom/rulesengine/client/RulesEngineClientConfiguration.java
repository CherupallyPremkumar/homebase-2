package com.homebase.ecom.rulesengine.client;

import com.homebase.ecom.rulesengine.api.service.DecisionService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RulesEngineClientConfiguration {
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
