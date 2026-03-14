package com.homebase.ecom.returnprocessing.service.postSaveHooks;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for ITEM_RECEIVED state.
 * Publishes events after item has been received at warehouse.
 */
public class ITEM_RECEIVEDReturnProcessingPostSaveHook implements PostSaveHook<ReturnProcessingSaga> {

    private static final Logger log = LoggerFactory.getLogger(ITEM_RECEIVEDReturnProcessingPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, ReturnProcessingSaga saga, TransientMap map) {
        log.info("Post-save: Item received for return request: {}", saga.getReturnRequestId());
        // TODO: Publish ITEM_RECEIVED event to notify inventory BC
    }
}
