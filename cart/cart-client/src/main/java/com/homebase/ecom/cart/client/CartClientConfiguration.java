package com.homebase.ecom.cart.client;

import com.homebase.ecom.cart.client.delegate.CartManagerClient;
import com.homebase.ecom.cart.client.delegate.CartManagerClientImpl;
import com.homebase.ecom.cart.service.CartService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cart client configuration following the Chenile process-delegate pattern.
 *
 * Creates two beans:
 * 1. cartServiceProxy — raw Chenile Proxy for CartService (low-level)
 * 2. cartManagerClient — typed business delegate with domain methods (recommended)
 */
@Configuration
public class CartClientConfiguration {

    @Bean
    public CartService cartServiceProxy(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            CartService.class,
            "_cartStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            null
        );
    }

    @Bean
    public CartManagerClient cartManagerClient() {
        return new CartManagerClientImpl();
    }
}
