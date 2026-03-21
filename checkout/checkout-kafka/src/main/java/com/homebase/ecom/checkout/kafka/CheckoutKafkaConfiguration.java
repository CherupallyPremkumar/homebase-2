package com.homebase.ecom.checkout.kafka;

import com.homebase.ecom.checkout.domain.port.CheckoutEventPublisherPort;
import org.chenile.pubsub.ChenilePub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.ObjectMapper;

/**
 * Provides Kafka checkout event publisher.
 * When checkout-kafka is on classpath, this bean takes precedence over InVM (@Primary).
 */
@Configuration
public class CheckoutKafkaConfiguration {

    @Bean
    @Primary
    CheckoutEventPublisherPort checkoutEventPublisherPort(ChenilePub chenilePub, ObjectMapper objectMapper) {
        return new KafkaCheckoutEventPublisher(chenilePub, objectMapper);
    }
}
