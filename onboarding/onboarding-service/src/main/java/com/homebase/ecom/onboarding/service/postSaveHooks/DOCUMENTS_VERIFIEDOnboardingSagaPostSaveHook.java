package com.homebase.ecom.onboarding.service.postSaveHooks;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DOCUMENTS_VERIFIEDOnboardingSagaPostSaveHook implements PostSaveHook<OnboardingSaga> {

    private static final Logger log = LoggerFactory.getLogger(DOCUMENTS_VERIFIEDOnboardingSagaPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, OnboardingSaga saga, TransientMap map) {
        log.info("DocumentsVerifiedEvent: Documents verified for application {} (supplier: {})",
                saga.getId(), saga.getSupplierName());
        map.put("eventType", "DocumentsVerifiedEvent");
    }
}
