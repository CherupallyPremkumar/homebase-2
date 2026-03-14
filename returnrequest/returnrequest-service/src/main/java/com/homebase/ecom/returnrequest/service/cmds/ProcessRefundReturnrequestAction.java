package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.ProcessRefundReturnrequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Handles the processRefund transition (RECEIVED -> REFUNDED).
 * Calculates refund amount, records refund transaction details.
 */
public class ProcessRefundReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        ProcessRefundReturnrequestPayload> {

    private static final Logger log = LoggerFactory.getLogger(ProcessRefundReturnrequestAction.class);

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             ProcessRefundReturnrequestPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Set refund amount from payload or use previously calculated amount
        if (payload.getRefundAmount() != null) {
            returnrequest.refundAmount = payload.getRefundAmount();
        } else if (returnrequest.refundAmount == null && returnrequest.itemPrice != null) {
            // Fallback: calculate from item price
            returnrequest.refundAmount = returnrequest.itemPrice.multiply(
                    BigDecimal.valueOf(returnrequest.quantity != null ? returnrequest.quantity : 1));
        }

        // Record refund method
        if (payload.getRefundMethod() != null) {
            returnrequest.refundMethod = payload.getRefundMethod();
        } else if (returnrequest.refundMethod == null) {
            returnrequest.refundMethod = "ORIGINAL_PAYMENT";
        }

        // Generate refund transaction ID
        returnrequest.refundTransactionId = "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        returnrequest.refundProcessedAt = LocalDateTime.now();

        returnrequest.getTransientMap().previousPayload = payload;
        log.info("Return request {} refund processed: amount={}, method={}, txnId={}",
                returnrequest.getId(), returnrequest.refundAmount,
                returnrequest.refundMethod, returnrequest.refundTransactionId);
    }
}
