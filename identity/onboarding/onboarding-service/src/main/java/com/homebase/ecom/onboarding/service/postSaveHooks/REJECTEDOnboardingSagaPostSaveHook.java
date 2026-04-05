package com.homebase.ecom.onboarding.service.postSaveHooks;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.port.NotificationPort;
import com.homebase.ecom.onboarding.port.OnboardingEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post-save hook for REJECTED state.
 * Notifies seller with reason and publishes ONBOARDING_REJECTED event via domain port.
 */
public class REJECTEDOnboardingSagaPostSaveHook implements PostSaveHook<OnboardingSaga> {

    private static final Logger log = LoggerFactory.getLogger(REJECTEDOnboardingSagaPostSaveHook.class);

    @Autowired
    private NotificationPort notificationPort;

    @Autowired
    private OnboardingEventPublisherPort onboardingEventPublisherPort;

    @Override
    public void execute(State startState, State endState, OnboardingSaga saga, TransientMap map) {
        log.info("OnboardingRejected: Application {} rejected. Reason: {}",
                saga.getId(), saga.getRejectionReason());

        if (notificationPort != null) {
            notificationPort.notifyOnboardingRejected(saga.getId(), saga.getBusinessName(), saga.getRejectionReason());
        }

        if (onboardingEventPublisherPort != null) {
            onboardingEventPublisherPort.publishOnboardingRejected(saga);
        }
    }
}
