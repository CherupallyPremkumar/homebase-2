package com.homebase.ecom.onboarding.infrastructure.integration;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.port.OnboardingEventPublisherPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.OnboardingEvent;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Infrastructure adapter for publishing onboarding domain events to Kafka.
 * Uses ChenilePub for event delivery to onboarding.events topic.
 * No @Component -- wired explicitly via @Bean in OnboardingInfrastructureConfiguration.
 */
public class KafkaOnboardingEventPublisher implements OnboardingEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaOnboardingEventPublisher.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public KafkaOnboardingEventPublisher(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishOnboardingStarted(OnboardingSaga saga) {
        OnboardingEvent event = new OnboardingEvent(saga.getId(), OnboardingEvent.ONBOARDING_STARTED);
        event.setBusinessName(saga.getBusinessName());
        publish(saga.getId(), event, OnboardingEvent.ONBOARDING_STARTED);
    }

    @Override
    public void publishOnboardingCompleted(OnboardingSaga saga) {
        OnboardingEvent event = new OnboardingEvent(saga.getId(), OnboardingEvent.ONBOARDING_COMPLETED);
        event.setSupplierId(saga.getSupplierId());
        event.setBusinessName(saga.getBusinessName());
        publish(saga.getId(), event, OnboardingEvent.ONBOARDING_COMPLETED);
    }

    @Override
    public void publishOnboardingRejected(OnboardingSaga saga) {
        OnboardingEvent event = new OnboardingEvent(saga.getId(), OnboardingEvent.ONBOARDING_REJECTED);
        event.setBusinessName(saga.getBusinessName());
        event.setReason(saga.getRejectionReason());
        publish(saga.getId(), event, OnboardingEvent.ONBOARDING_REJECTED);
    }

    private void publish(String key, Object event, String eventType) {
        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.ONBOARDING_EVENTS, body,
                    Map.of("key", key, "eventType", eventType));
            log.info("Published {} for onboarding {}", eventType, key);
        } catch (JacksonException e) {
            log.error("Failed to serialize {} for onboarding {}", eventType, key, e);
        }
    }
}
