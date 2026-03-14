package com.homebase.ecom.returnprocessing.service.postSaveHooks;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for COMPLETED state.
 * Publishes the final RETURN_PROCESSING_COMPLETED event.
 */
public class COMPLETEDReturnProcessingPostSaveHook implements PostSaveHook<ReturnProcessingSaga> {

    private static final Logger log = LoggerFactory.getLogger(COMPLETEDReturnProcessingPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, ReturnProcessingSaga saga, TransientMap map) {
        log.info("Post-save: Return processing completed for return request: {}, refundId: {}, refundAmount: {}",
                saga.getReturnRequestId(), saga.getRefundId(), saga.getRefundAmount());
        // TODO: Publish RETURN_PROCESSING_COMPLETED event to notify all interested BCs
    }
}
