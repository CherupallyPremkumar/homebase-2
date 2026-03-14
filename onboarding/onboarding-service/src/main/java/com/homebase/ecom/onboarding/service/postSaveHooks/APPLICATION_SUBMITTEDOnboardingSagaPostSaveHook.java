package com.homebase.ecom.onboarding.service.postSaveHooks;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APPLICATION_SUBMITTEDOnboardingSagaPostSaveHook implements PostSaveHook<OnboardingSaga> {

    private static final Logger log = LoggerFactory.getLogger(APPLICATION_SUBMITTEDOnboardingSagaPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, OnboardingSaga saga, TransientMap map) {
        if (startState == null) {
            log.info("ApplicationSubmittedEvent: New onboarding application {} from {}",
                    saga.getId(), saga.getSupplierName());
        } else {
            log.info("ApplicationResubmittedEvent: Onboarding application {} resubmitted by {}",
                    saga.getId(), saga.getSupplierName());
        }
        map.put("eventType", startState == null ? "ApplicationSubmittedEvent" : "ApplicationResubmittedEvent");
    }
}
