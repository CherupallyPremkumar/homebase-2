package com.homebase.ecom.cart.client;

import com.homebase.ecom.cart.client.delegate.CartManagerClient;
import com.homebase.ecom.cart.client.delegate.CartManagerClientImpl;
import com.homebase.ecom.cart.service.CartService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.chenile.query.service.SearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cart client configuration following the Chenile process-delegate pattern.
 *
 * Creates three proxy beans for cross-BC consumption:
 * 1. cartServiceProxy — command side (STM mutations)
 * 2. cartSearchServiceProxy — query side (CQRS reads)
 * 3. cartManagerClient — typed business delegate with domain methods (recommended)
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
            "http://localhost:8080"
        );
    }

    @Bean
    public CartManagerClient cartManagerClient() {
        return new CartManagerClientImpl();
    }

    @Bean
    public SearchService cartSearchServiceProxy(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            SearchService.class,
            "searchService",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
