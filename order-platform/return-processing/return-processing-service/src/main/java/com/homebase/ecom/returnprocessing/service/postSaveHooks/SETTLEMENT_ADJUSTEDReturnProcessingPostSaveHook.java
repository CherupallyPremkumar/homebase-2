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
 * Post-save hook for SETTLEMENT_ADJUSTED state.
 * Publishes SETTLEMENT_ADJUSTED event via domain port to notify payment BC.
 */
public class SETTLEMENT_ADJUSTEDReturnProcessingPostSaveHook implements PostSaveHook<ReturnProcessingSaga> {

    private static final Logger log = LoggerFactory.getLogger(SETTLEMENT_ADJUSTEDReturnProcessingPostSaveHook.class);

    @Autowired
    private ReturnProcessingEventPublisherPort returnProcessingEventPublisherPort;

    @Override
    public void execute(State startState, State endState, ReturnProcessingSaga saga, TransientMap map) {
        log.info("Post-save: Settlement adjusted for return request: {}", saga.getReturnRequestId());

        if (returnProcessingEventPublisherPort != null) {
            returnProcessingEventPublisherPort.publishSettlementAdjusted(saga);
        }
    }
}
