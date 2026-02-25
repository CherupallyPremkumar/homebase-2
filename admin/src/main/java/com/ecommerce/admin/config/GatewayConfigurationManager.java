package com.ecommerce.admin.config;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GatewayConfigurationManager {

    private static final String ACTIVE_GATEWAY_KEY = "payment:gateway:active";
    private static final String DEFAULT_GATEWAY = "stripe";

    private final StringRedisTemplate redisTemplate;

    public GatewayConfigurationManager(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getActiveGateway() {
        return Optional.ofNullable(redisTemplate.opsForValue().get(ACTIVE_GATEWAY_KEY))
                .orElse(DEFAULT_GATEWAY);
    }

    public void setActiveGateway(String gatewayType) {
        // Validation could be added here to ensure gatewayType is supported
        redisTemplate.opsForValue().set(ACTIVE_GATEWAY_KEY, gatewayType);
    }

    public boolean isStripeActive() {
        return "stripe".equalsIgnoreCase(getActiveGateway());
    }

    public boolean isRazorpayActive() {
        return "razorpay".equalsIgnoreCase(getActiveGateway());
    }
}
