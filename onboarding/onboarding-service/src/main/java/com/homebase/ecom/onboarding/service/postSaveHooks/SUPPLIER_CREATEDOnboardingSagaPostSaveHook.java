package com.homebase.ecom.onboarding.service.postSaveHooks;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SUPPLIER_CREATEDOnboardingSagaPostSaveHook implements PostSaveHook<OnboardingSaga> {

    private static final Logger log = LoggerFactory.getLogger(SUPPLIER_CREATEDOnboardingSagaPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, OnboardingSaga saga, TransientMap map) {
        log.info("SupplierCreatedEvent: Supplier record {} created for application {}",
                saga.getSupplierId(), saga.getId());
        map.put("eventType", "SupplierCreatedEvent");
        map.put("supplierId", saga.getSupplierId());
    }
}
