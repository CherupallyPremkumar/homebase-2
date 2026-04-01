package com.homebase.ecom.returnrequest.client;

import org.chenile.proxy.builder.ProxyBuilder;
import org.chenile.workflow.api.StateEntityService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReturnRequestClientConfiguration {
    @Bean
    @Qualifier("returnrequestServiceClient")
    public StateEntityService<?> returnrequestServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            StateEntityService.class,
            "_returnrequestStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
