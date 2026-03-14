package com.homebase.ecom.returnprocessing.service.postSaveHooks;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for INVENTORY_RESTOCKED state.
 * Publishes events after inventory has been restocked.
 */
public class INVENTORY_RESTOCKEDReturnProcessingPostSaveHook implements PostSaveHook<ReturnProcessingSaga> {

    private static final Logger log = LoggerFactory.getLogger(INVENTORY_RESTOCKEDReturnProcessingPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, ReturnProcessingSaga saga, TransientMap map) {
        log.info("Post-save: Inventory restocked for return request: {}", saga.getReturnRequestId());
        // TODO: Publish INVENTORY_RESTOCKED event to notify settlement BC
    }
}
