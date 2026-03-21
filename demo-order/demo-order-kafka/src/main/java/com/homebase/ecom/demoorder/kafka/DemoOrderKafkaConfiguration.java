package com.homebase.ecom.demoorder.kafka;

import com.homebase.ecom.demoorder.port.DemoOrderEventPublisherPort;
import org.chenile.pubsub.ChenilePub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.ObjectMapper;

/**
 * Provides Kafka demo-order event publisher.
 * When demo-order-kafka is on classpath, this bean takes precedence over InVM (@Primary).
 */
@Configuration
public class DemoOrderKafkaConfiguration {

    @Bean
    @Primary
    DemoOrderEventPublisherPort demoOrderEventPublisherPort(
            ChenilePub chenilePub, ObjectMapper objectMapper) {
        return new KafkaDemoOrderEventPublisher(chenilePub, objectMapper);
    }
}
