package com.homebase.ecom.order.infrastructure.configuration;

import com.homebase.ecom.order.infrastructure.integration.KafkaOrderEventPublisher;
import com.homebase.ecom.order.infrastructure.integration.OrderFulfillmentAdapter;
import com.homebase.ecom.order.infrastructure.integration.OrderNotificationAdapter;
import com.homebase.ecom.order.infrastructure.integration.OrderPaymentAdapter;
import com.homebase.ecom.order.port.FulfillmentPort;
import com.homebase.ecom.order.port.NotificationPort;
import com.homebase.ecom.order.port.OrderEventPublisherPort;
import com.homebase.ecom.order.port.PaymentPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Infrastructure layer configuration for the Order bounded context.
 *
 * Wires infrastructure adapters to domain port interfaces.
 * Each adapter is a pure translator / anti-corruption layer between
 * the order domain and external bounded contexts.
 */
@Configuration
public class OrderInfrastructureConfiguration {

    @Bean("orderFulfillmentPort")
    FulfillmentPort orderFulfillmentPort() {
        return new OrderFulfillmentAdapter();
    }

    @Bean("orderNotificationPort")
    NotificationPort orderNotificationPort() {
        return new OrderNotificationAdapter();
    }

    @Bean("orderPaymentPort")
    PaymentPort orderPaymentPort() {
        return new OrderPaymentAdapter();
    }

    @SuppressWarnings("rawtypes")
    @Bean("orderEventPublisherPort")
    OrderEventPublisherPort orderEventPublisherPort(KafkaTemplate kafkaTemplate) {
        return new KafkaOrderEventPublisher(kafkaTemplate);
    }
}
