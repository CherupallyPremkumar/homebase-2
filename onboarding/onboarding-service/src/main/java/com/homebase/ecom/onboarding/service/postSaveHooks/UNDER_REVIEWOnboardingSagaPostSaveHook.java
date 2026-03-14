package com.homebase.ecom.onboarding.service.postSaveHooks;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UNDER_REVIEWOnboardingSagaPostSaveHook implements PostSaveHook<OnboardingSaga> {

    private static final Logger log = LoggerFactory.getLogger(UNDER_REVIEWOnboardingSagaPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, OnboardingSaga saga, TransientMap map) {
        log.info("ApplicationUnderReviewEvent: Application {} submitted for admin review",
                saga.getId());
        map.put("eventType", "ApplicationUnderReviewEvent");
    }
}
