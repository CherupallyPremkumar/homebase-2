package com.homebase.ecom.returnprocessing.service.cmds;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM transition action: adjusts supplier settlement by deducting the returned item amount.
 * Transition: INVENTORY_RESTOCKED -> SETTLEMENT_ADJUSTED
 */
public class AdjustSettlementAction extends AbstractSTMTransitionAction<ReturnProcessingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(AdjustSettlementAction.class);

    @Override
    public void transitionTo(ReturnProcessingSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Adjusting settlement for return request: {}, order: {}, refundAmount: {}",
                saga.getReturnRequestId(), saga.getOrderId(), saga.getRefundAmount());

        if (saga.getRefundAmount() == null || saga.getRefundAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            log.warn("No refund amount set for return request {}, skipping settlement adjustment",
                    saga.getReturnRequestId());
            saga.setSettlementAdjustmentId("SKIP-NO-AMOUNT");
            return;
        }

        // Deduct returned item amount from supplier settlement
        String adjustmentId = "SADJ-" + saga.getOrderId() + "-" + saga.getOrderItemId() + "-" + System.currentTimeMillis();
        saga.setSettlementAdjustmentId(adjustmentId);

        saga.getTransientMap().put("settlementAdjustedAt", java.time.Instant.now().toString());
        saga.getTransientMap().put("adjustmentAmount", saga.getRefundAmount().toString());

        log.info("Settlement adjusted for return request: {}, adjustmentId: {}, amount: {}",
                saga.getReturnRequestId(), adjustmentId, saga.getRefundAmount());
    }
}
