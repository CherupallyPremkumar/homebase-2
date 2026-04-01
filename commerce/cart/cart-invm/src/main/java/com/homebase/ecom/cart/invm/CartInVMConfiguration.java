package com.homebase.ecom.cart.invm;

import com.homebase.ecom.cart.port.CartEventPublisherPort;
import org.chenile.core.event.EventProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * Provides InVM cart event publisher as default.
 * If cart-kafka is on classpath, its bean takes precedence (via @Primary).
 */
@Configuration
public class CartInVMConfiguration {

    @Bean
    @ConditionalOnMissingBean(CartEventPublisherPort.class)
    CartEventPublisherPort cartEventPublisherPort(EventProcessor eventProcessor, ObjectMapper objectMapper) {
        return new InVMCartEventPublisher(eventProcessor, objectMapper);
    }
}
