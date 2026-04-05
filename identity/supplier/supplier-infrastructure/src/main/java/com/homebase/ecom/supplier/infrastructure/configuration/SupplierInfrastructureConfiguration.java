package com.homebase.ecom.supplier.infrastructure.configuration;

import com.homebase.ecom.supplier.domain.port.NotificationPort;
import com.homebase.ecom.supplier.domain.port.PayoutPort;
import com.homebase.ecom.supplier.infrastructure.integration.LoggingPayoutAdapter;
import com.homebase.ecom.supplier.infrastructure.integration.SupplierNotificationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure layer configuration for the Supplier bounded context.
 *
 * Wires infrastructure adapters to domain port interfaces.
 * Each adapter is a pure translator / anti-corruption layer between
 * the supplier domain and external bounded contexts.
 */
@Configuration
public class SupplierInfrastructureConfiguration {

    @Bean("supplierNotificationPort")
    NotificationPort supplierNotificationPort() {
        return new SupplierNotificationAdapter();
    }

    @Bean("supplierPayoutPort")
    PayoutPort supplierPayoutPort() {
        return new LoggingPayoutAdapter();
    }
}
