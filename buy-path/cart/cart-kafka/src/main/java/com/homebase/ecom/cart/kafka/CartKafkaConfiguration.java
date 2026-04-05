package com.homebase.ecom.cart.kafka;

import com.homebase.ecom.cart.port.CartEventPublisherPort;
import org.chenile.pubsub.ChenilePub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.ObjectMapper;

/**
 * Provides Kafka cart event publisher.
 * When cart-kafka is on classpath, this bean takes precedence over InVM (@Primary).
 */
@Configuration
public class CartKafkaConfiguration {

    @Bean
    CartEventPublisherPort cartEventPublisherPort(ChenilePub chenilePub, ObjectMapper objectMapper) {
        return new KafkaCartEventPublisher(chenilePub, objectMapper);
    }
}
