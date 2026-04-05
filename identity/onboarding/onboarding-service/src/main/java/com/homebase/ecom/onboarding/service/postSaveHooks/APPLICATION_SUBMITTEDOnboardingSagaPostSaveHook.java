package com.homebase.ecom.onboarding.service.postSaveHooks;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.port.OnboardingEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post-save hook for APPLICATION_SUBMITTED state.
 * Publishes ONBOARDING_STARTED event via domain port.
 */
public class APPLICATION_SUBMITTEDOnboardingSagaPostSaveHook implements PostSaveHook<OnboardingSaga> {

    private static final Logger log = LoggerFactory.getLogger(APPLICATION_SUBMITTEDOnboardingSagaPostSaveHook.class);

    @Autowired
    private OnboardingEventPublisherPort onboardingEventPublisherPort;

    @Override
    public void execute(State startState, State endState, OnboardingSaga saga, TransientMap map) {
        log.info("OnboardingStarted: Application {} from business {}",
                saga.getId(), saga.getBusinessName());

        if (onboardingEventPublisherPort != null) {
            onboardingEventPublisherPort.publishOnboardingStarted(saga);
        }
    }
}
