package com.homebase.ecom.onboarding.port;

import com.homebase.ecom.onboarding.model.OnboardingSaga;

/**
 * Outbound port for publishing onboarding domain events.
 * Infrastructure layer provides Kafka implementation.
 * Domain/service layer depends only on this interface -- never on ChenilePub directly.
 */
public interface OnboardingEventPublisherPort {

    /**
     * Publishes event when an onboarding application is submitted.
     */
    void publishOnboardingStarted(OnboardingSaga saga);

    /**
     * Publishes event when onboarding is completed (supplier fully onboarded).
     */
    void publishOnboardingCompleted(OnboardingSaga saga);

    /**
     * Publishes event when an onboarding application is rejected.
     */
    void publishOnboardingRejected(OnboardingSaga saga);
}
