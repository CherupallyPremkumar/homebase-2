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
 * Post-save hook for COMPLETED state.
 * Publishes RETURN_PROCESSING_COMPLETED event via domain port to notify all interested BCs.
 */
public class COMPLETEDReturnProcessingPostSaveHook implements PostSaveHook<ReturnProcessingSaga> {

    private static final Logger log = LoggerFactory.getLogger(COMPLETEDReturnProcessingPostSaveHook.class);

    @Autowired
    private ReturnProcessingEventPublisherPort returnProcessingEventPublisherPort;

    @Override
    public void execute(State startState, State endState, ReturnProcessingSaga saga, TransientMap map) {
        log.info("Post-save: Return processing completed for return request: {}, refundId: {}, refundAmount: {}",
                saga.getReturnRequestId(), saga.getRefundId(), saga.getRefundAmount());

        if (returnProcessingEventPublisherPort != null) {
            returnProcessingEventPublisherPort.publishReturnProcessingCompleted(saga);
        }
    }
}
