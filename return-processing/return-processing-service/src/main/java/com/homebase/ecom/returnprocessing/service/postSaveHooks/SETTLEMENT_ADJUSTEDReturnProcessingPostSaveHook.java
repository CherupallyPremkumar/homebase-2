package com.homebase.ecom.returnprocessing.service.postSaveHooks;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for SETTLEMENT_ADJUSTED state.
 * Publishes events after supplier settlement has been adjusted.
 */
public class SETTLEMENT_ADJUSTEDReturnProcessingPostSaveHook implements PostSaveHook<ReturnProcessingSaga> {

    private static final Logger log = LoggerFactory.getLogger(SETTLEMENT_ADJUSTEDReturnProcessingPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, ReturnProcessingSaga saga, TransientMap map) {
        log.info("Post-save: Settlement adjusted for return request: {}", saga.getReturnRequestId());
        // TODO: Publish SETTLEMENT_ADJUSTED event to notify payment BC
    }
}
