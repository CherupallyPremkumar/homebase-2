package com.homebase.ecom.settlement.infrastructure.configuration;

import com.homebase.ecom.settlement.domain.port.SettlementEventPublisherPort;
import com.homebase.ecom.settlement.infrastructure.event.KafkaSettlementEventPublisher;
import org.chenile.pubsub.ChenilePub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * Infrastructure layer configuration for the Settlement bounded context.
 * Wires adapter implementations to domain port interfaces.
 *
 * <p>All beans are module-prefixed to avoid collisions in the monolith.</p>
 */
@Configuration
public class SettlementInfrastructureConfiguration {

    @Bean("settlementEventPublisherPort")
    SettlementEventPublisherPort settlementEventPublisherPort(ChenilePub chenilePub, ObjectMapper objectMapper) {
        return new KafkaSettlementEventPublisher(chenilePub, objectMapper);
    }
}
