package com.homebase.ecom.shipping.infrastructure.configuration;

import com.homebase.ecom.shipping.domain.port.CarrierPort;
import com.homebase.ecom.shipping.domain.port.NotificationPort;
import com.homebase.ecom.shipping.infrastructure.integration.LoggingCarrierAdapter;
import com.homebase.ecom.shipping.infrastructure.integration.ShippingNotificationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure layer configuration for the Shipping bounded context.
 *
 * Wires infrastructure adapters to domain port interfaces.
 * Each adapter is a pure translator / anti-corruption layer between
 * the shipping domain and external bounded contexts.
 */
@Configuration
public class ShippingInfrastructureConfiguration {

    @Bean("shippingCarrierPort")
    CarrierPort shippingCarrierPort() {
        return new LoggingCarrierAdapter();
    }

    @Bean("shippingNotificationPort")
    NotificationPort shippingNotificationPort() {
        return new ShippingNotificationAdapter();
    }
}
