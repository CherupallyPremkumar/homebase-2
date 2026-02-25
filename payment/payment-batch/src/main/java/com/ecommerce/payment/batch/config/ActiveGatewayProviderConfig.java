package com.ecommerce.payment.batch.config;

import com.ecommerce.payment.gateway.ActiveGatewayTypeProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveGatewayProviderConfig {

    @Bean
    @ConditionalOnMissingBean(ActiveGatewayTypeProvider.class)
    public ActiveGatewayTypeProvider activeGatewayTypeProvider(
            @Value("${app.payment.gateway.active:stripe}") String activeGatewayType) {
        return () -> activeGatewayType;
    }
}
