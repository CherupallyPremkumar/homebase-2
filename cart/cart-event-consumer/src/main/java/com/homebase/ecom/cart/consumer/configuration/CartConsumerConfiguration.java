package com.homebase.ecom.cart.consumer.configuration;

import com.homebase.ecom.cart.service.CartService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration for the cart event consumer module.
 * Enables Kafka listeners, scheduled tasks, and configures Chenile proxies.
 */
@Configuration
@EnableKafka
@EnableScheduling
@ComponentScan(basePackages = "com.homebase.ecom.cart.consumer")
public class CartConsumerConfiguration {

    /**
     * Proxy for CartService to trigger STM transitions.
     * Hits the "_cartStateEntityService_" which is registered in CartController.
     */
    @Bean
    public CartService cartService(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(CartService.class, "_cartStateEntityService_", null,
                ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY, null);
    }

}
