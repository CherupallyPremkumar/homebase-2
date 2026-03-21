package com.homebase.ecom.onboarding.client;

import com.homebase.ecom.onboarding.service.OnboardingService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnboardingClientConfiguration {
    @Bean
    public OnboardingService onboardingServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            OnboardingService.class,
            "_onboardingStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
