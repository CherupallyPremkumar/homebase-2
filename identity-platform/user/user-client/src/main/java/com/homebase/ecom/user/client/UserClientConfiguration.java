package com.homebase.ecom.user.client;

import com.homebase.ecom.user.service.UserService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserClientConfiguration {
    @Bean
    public UserService userServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            UserService.class,
            "_userStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
