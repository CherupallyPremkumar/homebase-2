package com.homebase.ecom.returnprocessing.service.postSaveHooks;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for REFUNDED state.
 * Publishes events after customer refund has been processed.
 */
public class REFUNDEDReturnProcessingPostSaveHook implements PostSaveHook<ReturnProcessingSaga> {

    private static final Logger log = LoggerFactory.getLogger(REFUNDEDReturnProcessingPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, ReturnProcessingSaga saga, TransientMap map) {
        log.info("Post-save: Refund processed for return request: {}, refundId: {}",
                saga.getReturnRequestId(), saga.getRefundId());
        // TODO: Publish REFUND_PROCESSED event
    }
}
