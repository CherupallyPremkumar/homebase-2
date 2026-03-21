package com.homebase.ecom.payment.service.impl;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;

public class DynamicGatewayResolver {

    private static final String ACTIVE_GATEWAY_KEY = "payment:gateway:active";
    private static final String DEFAULT_GATEWAY = "stripe";

    private final StringRedisTemplate redisTemplate;

    public DynamicGatewayResolver(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getActiveGatewayType() {
        return Optional.ofNullable(redisTemplate.opsForValue().get(ACTIVE_GATEWAY_KEY))
                .orElse(DEFAULT_GATEWAY);
    }
}
