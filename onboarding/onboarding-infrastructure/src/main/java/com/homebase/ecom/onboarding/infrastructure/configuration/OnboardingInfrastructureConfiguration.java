package com.homebase.ecom.onboarding.infrastructure.configuration;

import com.homebase.ecom.onboarding.infrastructure.integration.KafkaOnboardingEventPublisher;
import com.homebase.ecom.onboarding.port.OnboardingEventPublisherPort;
import org.chenile.pubsub.ChenilePub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * Infrastructure layer configuration for the Onboarding bounded context.
 *
 * Wires infrastructure adapters to domain port interfaces.
 * No @Component/@Service -- all beans declared explicitly via @Bean.
 */
@Configuration
public class OnboardingInfrastructureConfiguration {

    @Bean("onboardingEventPublisherPort")
    OnboardingEventPublisherPort onboardingEventPublisherPort(ChenilePub chenilePub, ObjectMapper objectMapper) {
        return new KafkaOnboardingEventPublisher(chenilePub, objectMapper);
    }
}
