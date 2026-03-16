package com.homebase.ecom.onboarding.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.OnboardingEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post-save hook for COMPLETED state.
 * Publishes ONBOARDING_COMPLETED event to onboarding.events topic.
 */
public class COMPLETEDOnboardingSagaPostSaveHook implements PostSaveHook<OnboardingSaga> {

    private static final Logger log = LoggerFactory.getLogger(COMPLETEDOnboardingSagaPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, OnboardingSaga saga, TransientMap map) {
        log.info("OnboardingCompleted: Application {} fully completed for business {}, supplier={}",
                saga.getId(), saga.getBusinessName(), saga.getSupplierId());

        if (chenilePub == null || objectMapper == null) return;

        OnboardingEvent event = new OnboardingEvent(saga.getId(), OnboardingEvent.ONBOARDING_COMPLETED);
        event.setSupplierId(saga.getSupplierId());
        event.setBusinessName(saga.getBusinessName());
        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.ONBOARDING_EVENTS, body,
                    Map.of("key", saga.getId(), "eventType", OnboardingEvent.ONBOARDING_COMPLETED));
        } catch (JacksonException e) {
            log.error("Failed to serialize ONBOARDING_COMPLETED event for id={}", saga.getId(), e);
        }
    }
}
