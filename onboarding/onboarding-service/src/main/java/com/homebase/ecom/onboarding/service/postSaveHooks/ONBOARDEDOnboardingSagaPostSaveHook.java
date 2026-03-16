package com.homebase.ecom.onboarding.service.postSaveHooks;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.port.NotificationPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post-save hook for ONBOARDED state.
 * Creates supplier entity and notifies supplier.
 */
public class ONBOARDEDOnboardingSagaPostSaveHook implements PostSaveHook<OnboardingSaga> {

    private static final Logger log = LoggerFactory.getLogger(ONBOARDEDOnboardingSagaPostSaveHook.class);

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, OnboardingSaga saga, TransientMap map) {
        log.info("Onboarded: Supplier {} ({}) has completed training and is onboarded",
                saga.getBusinessName(), saga.getId());

        if (notificationPort != null) {
            notificationPort.notifyOnboardingCompleted(saga.getId(), saga.getBusinessName(), saga.getSupplierId());
        }
    }
}
