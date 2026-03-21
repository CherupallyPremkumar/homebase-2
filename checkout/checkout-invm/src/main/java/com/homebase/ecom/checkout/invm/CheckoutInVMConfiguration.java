package com.homebase.ecom.checkout.invm;

import com.homebase.ecom.checkout.domain.port.CheckoutEventPublisherPort;
import org.chenile.core.event.EventProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * Provides InVM checkout event publisher as default.
 * If checkout-kafka is on classpath, its bean takes precedence (via @Primary).
 */
@Configuration
public class CheckoutInVMConfiguration {

    @Bean
    @ConditionalOnMissingBean(CheckoutEventPublisherPort.class)
    CheckoutEventPublisherPort checkoutEventPublisherPort(EventProcessor eventProcessor, ObjectMapper objectMapper) {
        return new InVMCheckoutEventPublisher(eventProcessor, objectMapper);
    }
}
