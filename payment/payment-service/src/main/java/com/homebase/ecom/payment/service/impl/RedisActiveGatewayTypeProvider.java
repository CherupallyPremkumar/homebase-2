package com.homebase.ecom.payment.service.impl;

import com.homebase.ecom.payment.gateway.ActiveGatewayTypeProvider;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Uses Redis-backed DynamicGatewayResolver as the source of truth for the active gateway.
 */
@Primary
@Component
public class RedisActiveGatewayTypeProvider implements ActiveGatewayTypeProvider {

    private final DynamicGatewayResolver dynamicGatewayResolver;

    public RedisActiveGatewayTypeProvider(DynamicGatewayResolver dynamicGatewayResolver) {
        this.dynamicGatewayResolver = dynamicGatewayResolver;
    }

    @Override
    public String getActiveGatewayType() {
        return dynamicGatewayResolver.getActiveGatewayType();
    }
}
