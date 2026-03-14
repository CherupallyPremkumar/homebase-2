package com.homebase.ecom.returnrequest.service.postSaveHooks;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.service.event.RefundProcessedEvent;
import com.homebase.ecom.returnrequest.service.event.ReturnRequestEventPublisher;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PostSaveHook for REFUNDED state.
 * Publishes RefundProcessedEvent after the refund has been processed.
 */
public class REFUNDEDReturnrequestPostSaveHook implements PostSaveHook<Returnrequest> {

    private static final Logger log = LoggerFactory.getLogger(REFUNDEDReturnrequestPostSaveHook.class);

    @Autowired
    private ReturnRequestEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, Returnrequest returnrequest, TransientMap map) {
        RefundProcessedEvent event = new RefundProcessedEvent(
                returnrequest.getId(),
                returnrequest.orderId,
                returnrequest.refundAmount,
                returnrequest.refundMethod,
                returnrequest.refundTransactionId
        );
        log.info("Return request {} refund processed, publishing RefundProcessedEvent", returnrequest.getId());
        eventPublisher.publishRefundProcessed(event);
    }
}
