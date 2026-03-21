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
 * Post-save hook for COMPLETED state.
 * Publishes ONBOARDING_COMPLETED event via domain port.
 */
public class COMPLETEDOnboardingSagaPostSaveHook implements PostSaveHook<OnboardingSaga> {

    private static final Logger log = LoggerFactory.getLogger(COMPLETEDOnboardingSagaPostSaveHook.class);

    @Autowired
    private OnboardingEventPublisherPort onboardingEventPublisherPort;

    @Override
    public void execute(State startState, State endState, OnboardingSaga saga, TransientMap map) {
        log.info("OnboardingCompleted: Application {} fully completed for business {}, supplier={}",
                saga.getId(), saga.getBusinessName(), saga.getSupplierId());

        if (onboardingEventPublisherPort != null) {
            onboardingEventPublisherPort.publishOnboardingCompleted(saga);
        }
    }
}
