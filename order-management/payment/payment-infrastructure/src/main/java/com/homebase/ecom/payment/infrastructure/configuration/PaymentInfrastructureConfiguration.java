package com.homebase.ecom.payment.infrastructure.configuration;

import com.homebase.ecom.payment.domain.port.NotificationPort;
import com.homebase.ecom.payment.domain.port.PaymentEventPublisherPort;
import com.homebase.ecom.payment.domain.port.PaymentRepositoryPort;
import com.homebase.ecom.payment.infrastructure.adapter.KafkaPaymentEventPublisher;
import com.homebase.ecom.payment.infrastructure.adapter.LoggingNotificationAdapter;
import com.homebase.ecom.payment.infrastructure.persistence.adapter.PaymentQueryAdapter;
import com.homebase.ecom.payment.infrastructure.persistence.mapper.PaymentMapper;
import com.homebase.ecom.payment.infrastructure.persistence.repository.PaymentJpaRepository;
import org.chenile.pubsub.ChenilePub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * Infrastructure layer configuration for the Payment bounded context.
 * Wires adapter implementations to domain port interfaces.
 *
 * <p>All beans are module-prefixed to avoid collisions in the monolith.</p>
 */
@Configuration
public class PaymentInfrastructureConfiguration {

    @Bean("paymentNotificationPort")
    NotificationPort paymentNotificationPort() {
        return new LoggingNotificationAdapter();
    }

    @Bean("paymentRepositoryPort")
    PaymentRepositoryPort paymentRepositoryPort(PaymentJpaRepository jpaRepository, PaymentMapper paymentMapper) {
        return new PaymentQueryAdapter(jpaRepository, paymentMapper);
    }

    @Bean("paymentEventPublisherPort")
    @ConditionalOnBean(ChenilePub.class)
    PaymentEventPublisherPort paymentEventPublisherPort(
            ChenilePub chenilePub,
            ObjectMapper objectMapper) {
        return new KafkaPaymentEventPublisher(chenilePub, objectMapper);
    }
}
