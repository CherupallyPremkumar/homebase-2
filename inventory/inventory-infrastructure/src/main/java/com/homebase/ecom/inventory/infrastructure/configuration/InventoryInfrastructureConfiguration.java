package com.homebase.ecom.inventory.infrastructure.configuration;

import com.homebase.ecom.inventory.domain.port.InventoryEventPublisherPort;
import com.homebase.ecom.inventory.domain.port.InventoryPolicyPort;
import com.homebase.ecom.inventory.infrastructure.adapter.InventoryPolicyDecisionAdapter;
import com.homebase.ecom.inventory.infrastructure.event.KafkaInventoryEventPublisher;
import com.homebase.ecom.rulesengine.api.service.DecisionService;
import org.chenile.pubsub.ChenilePub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * Infrastructure layer configuration for the Inventory bounded context.
 * Wires adapter implementations to domain port interfaces.
 *
 * <p>All beans are module-prefixed to avoid collisions in the monolith.</p>
 */
@Configuration
public class InventoryInfrastructureConfiguration {

    @Bean("inventoryPolicyPort")
    InventoryPolicyPort inventoryPolicyPort(DecisionService decisionServiceClient) {
        return new InventoryPolicyDecisionAdapter(decisionServiceClient);
    }

    @Bean("inventoryEventPublisherPort")
    InventoryEventPublisherPort inventoryEventPublisherPort(ChenilePub chenilePub, ObjectMapper objectMapper) {
        return new KafkaInventoryEventPublisher(chenilePub, objectMapper);
    }
}
