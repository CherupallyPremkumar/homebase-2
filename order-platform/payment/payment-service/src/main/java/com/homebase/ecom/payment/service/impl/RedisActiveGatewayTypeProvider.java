package com.homebase.ecom.payment.service.impl;

import com.homebase.ecom.payment.gateway.ActiveGatewayTypeProvider;

/**
 * Uses Redis-backed DynamicGatewayResolver as the source of truth for the active gateway.
 * Wired as @Bean in PaymentConfiguration.
 */
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
