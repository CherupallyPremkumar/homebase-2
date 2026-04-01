package com.homebase.ecom.returnprocessing.infrastructure.configuration;

import com.homebase.ecom.returnprocessing.infrastructure.integration.KafkaReturnProcessingEventPublisher;
import com.homebase.ecom.returnprocessing.port.ReturnProcessingEventPublisherPort;
import org.chenile.pubsub.ChenilePub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * Infrastructure layer configuration for the Return Processing bounded context.
 *
 * Wires infrastructure adapters to domain port interfaces.
 * No @Component/@Service -- all beans declared explicitly via @Bean.
 */
@Configuration
public class ReturnProcessingInfrastructureConfiguration {

    @Bean("returnProcessingEventPublisherPort")
    ReturnProcessingEventPublisherPort returnProcessingEventPublisherPort(
            ChenilePub chenilePub, ObjectMapper objectMapper) {
        return new KafkaReturnProcessingEventPublisher(chenilePub, objectMapper);
    }
}
