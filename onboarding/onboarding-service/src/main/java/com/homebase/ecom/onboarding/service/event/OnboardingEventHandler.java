package com.homebase.ecom.onboarding.service.event;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.shared.event.*;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * Chenile event handler for onboarding cross-service events.
 * Registered via onboardingEventService.json — operations subscribe to Kafka topics
 * through chenile-kafka auto-subscription.
 *
 * Bean name "onboardingEventService" must match the service JSON id.
 */
public class OnboardingEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OnboardingEventHandler.class);

    private final StateEntityServiceImpl<OnboardingSaga> onboardingStateEntityService;
    private final ObjectMapper objectMapper;

    public OnboardingEventHandler(
            @Qualifier("_onboardingStateEntityService_") StateEntityServiceImpl<OnboardingSaga> onboardingStateEntityService,
            ObjectMapper objectMapper) {
        this.onboardingStateEntityService = onboardingStateEntityService;
        this.objectMapper = objectMapper;
    }

    // ── supplier.events ────────────────────────────────────────────────────

    /**
     * Handles supplier events. When SUPPLIER_APPROVED is received, triggers
     * training phase for the corresponding onboarding application.
     */
    @Transactional
    public void handleSupplierEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        try {
            switch (envelope.getEventType()) {
                case SupplierApprovedEvent.EVENT_TYPE: {
                    SupplierApprovedEvent event = objectMapper.convertValue(
                            envelope.getPayload(), SupplierApprovedEvent.class);
                    log.info("Onboarding: received SupplierApprovedEvent for supplier: {}, onboarding: {}",
                            event.getSupplierId(), event.getOnboardingId());

                    if (event.getOnboardingId() != null) {
                        try {
                            onboardingStateEntityService.processById(
                                    event.getOnboardingId(), "startTraining", null);
                            log.info("Training started for onboarding: {}", event.getOnboardingId());
                        } catch (Exception e) {
                            log.warn("Failed to start training for onboarding {}: {}",
                                    event.getOnboardingId(), e.getMessage());
                        }
                    }
                    break;
                }
                default:
                    // Ignore unknown supplier event types
            }
        } catch (Exception e) {
            log.error("Error processing supplier event: {}", envelope.getEventType(), e);
        }
    }
}
