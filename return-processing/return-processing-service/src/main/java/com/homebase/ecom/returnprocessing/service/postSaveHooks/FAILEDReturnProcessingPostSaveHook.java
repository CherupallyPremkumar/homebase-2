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
 * Post-save hook for FAILED state.
 * Publishes RETURN_PROCESSING_FAILED event via domain port for alerting/monitoring.
 */
public class FAILEDReturnProcessingPostSaveHook implements PostSaveHook<ReturnProcessingSaga> {

    private static final Logger log = LoggerFactory.getLogger(FAILEDReturnProcessingPostSaveHook.class);

    @Autowired
    private ReturnProcessingEventPublisherPort returnProcessingEventPublisherPort;

    @Override
    public void execute(State startState, State endState, ReturnProcessingSaga saga, TransientMap map) {
        log.warn("Post-save: Return processing FAILED for return request: {}, error: {}",
                saga.getReturnRequestId(), saga.getErrorMessage());

        if (returnProcessingEventPublisherPort != null) {
            String previousState = startState != null ? startState.getStateId() : "";
            returnProcessingEventPublisherPort.publishReturnProcessingFailed(saga, previousState);
        }
    }
}
