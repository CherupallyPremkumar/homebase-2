package com.homebase.ecom.review.client;

import com.homebase.ecom.review.service.ReviewService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReviewClientConfiguration {
    @Bean
    public ReviewService reviewServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            ReviewService.class,
            "_reviewStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
