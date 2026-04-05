package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.CompleteReturnPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the completeReturn transition (REFUND_INITIATED -> COMPLETED).
 * Triggered by return-processing saga when refund is confirmed.
 */
public class CompleteReturnReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        CompleteReturnPayload> {

    private static final Logger log = LoggerFactory.getLogger(CompleteReturnReturnrequestAction.class);

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             CompleteReturnPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        returnrequest.getTransientMap().previousPayload = payload;
        log.info("Return request {} completed. Refund transaction: {}",
                returnrequest.getId(),
                payload.getRefundTransactionId() != null ? payload.getRefundTransactionId() : "N/A");
    }
}
