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
 * Post-save hook for INVENTORY_RESTOCKED state.
 * Publishes INVENTORY_RESTOCKED event via domain port to notify settlement BC.
 */
public class INVENTORY_RESTOCKEDReturnProcessingPostSaveHook implements PostSaveHook<ReturnProcessingSaga> {

    private static final Logger log = LoggerFactory.getLogger(INVENTORY_RESTOCKEDReturnProcessingPostSaveHook.class);

    @Autowired
    private ReturnProcessingEventPublisherPort returnProcessingEventPublisherPort;

    @Override
    public void execute(State startState, State endState, ReturnProcessingSaga saga, TransientMap map) {
        log.info("Post-save: Inventory restocked for return request: {}", saga.getReturnRequestId());

        if (returnProcessingEventPublisherPort != null) {
            returnProcessingEventPublisherPort.publishInventoryRestocked(saga);
        }
    }
}
