package com.homebase.ecom.returnprocessing.service.postSaveHooks;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import com.homebase.ecom.returnprocessing.port.ReturnProcessingEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post-save hook for PICKUP_SCHEDULED state.
 * Publishes PICKUP_SCHEDULED event via domain port to notify return-request BC.
 */
public class PICKUP_SCHEDULEDReturnProcessingPostSaveHook implements PostSaveHook<ReturnProcessingSaga> {

    private static final Logger log = LoggerFactory.getLogger(PICKUP_SCHEDULEDReturnProcessingPostSaveHook.class);

    @Autowired
    private ReturnProcessingEventPublisherPort returnProcessingEventPublisherPort;

    @Override
    public void execute(State startState, State endState, ReturnProcessingSaga saga, TransientMap map) {
        log.info("Post-save: Pickup scheduled for return request: {}", saga.getReturnRequestId());

        if (returnProcessingEventPublisherPort != null) {
            returnProcessingEventPublisherPort.publishPickupScheduled(saga);
        }
    }
}
