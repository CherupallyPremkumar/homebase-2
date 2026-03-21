package com.homebase.ecom.returnprocessing.client;

import com.homebase.ecom.returnprocessing.service.ReturnProcessingService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReturnProcessingClientConfiguration {
    @Bean
    public ReturnProcessingService returnProcessingServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            ReturnProcessingService.class,
            "_returnProcessingStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
