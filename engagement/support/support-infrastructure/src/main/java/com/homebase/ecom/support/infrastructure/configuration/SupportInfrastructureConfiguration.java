package com.homebase.ecom.support.infrastructure.configuration;

import com.homebase.ecom.support.domain.port.AgentAssignmentPort;
import com.homebase.ecom.support.domain.port.NotificationPort;
import com.homebase.ecom.support.domain.port.SupportEventPublisherPort;
import com.homebase.ecom.support.infrastructure.integration.KafkaSupportEventPublisher;
import com.homebase.ecom.support.infrastructure.integration.LoggingAgentAssignmentAdapter;
import com.homebase.ecom.support.infrastructure.integration.SupportNotificationAdapter;
import org.chenile.pubsub.ChenilePub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * Infrastructure layer configuration for the Support bounded context.
 *
 * Wires infrastructure adapters to domain port interfaces.
 * Each adapter is a pure translator / anti-corruption layer between
 * the support domain and external bounded contexts.
 */
@Configuration
public class SupportInfrastructureConfiguration {

    @Bean("supportAgentAssignmentPort")
    AgentAssignmentPort supportAgentAssignmentPort() {
        return new LoggingAgentAssignmentAdapter();
    }

    @Bean("supportNotificationPort")
    NotificationPort supportNotificationPort() {
        return new SupportNotificationAdapter();
    }

    @Bean("supportEventPublisherPort")
    SupportEventPublisherPort supportEventPublisherPort(ChenilePub chenilePub, ObjectMapper objectMapper) {
        return new KafkaSupportEventPublisher(chenilePub, objectMapper);
    }
}
