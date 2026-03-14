package com.homebase.ecom.returnprocessing.service.postSaveHooks;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for FAILED state.
 * Publishes failure event for alerting/monitoring.
 */
public class FAILEDReturnProcessingPostSaveHook implements PostSaveHook<ReturnProcessingSaga> {

    private static final Logger log = LoggerFactory.getLogger(FAILEDReturnProcessingPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, ReturnProcessingSaga saga, TransientMap map) {
        log.warn("Post-save: Return processing FAILED for return request: {}, error: {}",
                saga.getReturnRequestId(), saga.getErrorMessage());
        // TODO: Publish RETURN_PROCESSING_FAILED event for monitoring/alerting
    }
}
