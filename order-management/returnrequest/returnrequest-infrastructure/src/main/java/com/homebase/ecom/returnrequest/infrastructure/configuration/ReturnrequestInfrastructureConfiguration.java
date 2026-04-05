package com.homebase.ecom.returnrequest.infrastructure.configuration;

import com.homebase.ecom.returnrequest.domain.port.InventoryPort;
import com.homebase.ecom.returnrequest.domain.port.NotificationPort;
import com.homebase.ecom.returnrequest.domain.port.RefundPort;
import com.homebase.ecom.returnrequest.infrastructure.adapter.InventoryAdapter;
import com.homebase.ecom.returnrequest.infrastructure.adapter.NotificationAdapter;
import com.homebase.ecom.returnrequest.infrastructure.adapter.RefundAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure layer: wires adapters to domain ports for the ReturnRequest bounded context.
 *
 * Each adapter is a pure translator between domain ports and external infrastructure.
 * No @Component/@Service -- all beans declared explicitly via @Bean.
 */
@Configuration
public class ReturnrequestInfrastructureConfiguration {

    @Bean("returnrequestRefundPort")
    RefundPort returnrequestRefundPort() {
        return new RefundAdapter();
    }

    @Bean("returnrequestInventoryPort")
    InventoryPort returnrequestInventoryPort() {
        return new InventoryAdapter();
    }

    @Bean("returnrequestNotificationPort")
    NotificationPort returnrequestNotificationPort() {
        return new NotificationAdapter();
    }
}
