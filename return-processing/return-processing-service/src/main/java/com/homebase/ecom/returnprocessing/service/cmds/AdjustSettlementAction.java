package com.homebase.ecom.returnprocessing.service.cmds;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import com.homebase.ecom.returnprocessing.port.ReturnProcessingEventPublisherPort;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * STM transition action: requests settlement adjustment for the return.
 * Transition: INVENTORY_RESTOCKED -> SETTLEMENT_ADJUSTED
 *
 * Validates refund amount then publishes SETTLEMENT_ADJUSTMENT_REQUESTED event.
 * Settlement BC consumes this event and adjusts the supplier payout.
 */
public class AdjustSettlementAction extends AbstractSTMTransitionAction<ReturnProcessingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(AdjustSettlementAction.class);

    private final ReturnProcessingEventPublisherPort eventPublisher;

    public AdjustSettlementAction(ReturnProcessingEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void transitionTo(ReturnProcessingSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Requesting settlement adjustment for return request: {}, order: {}, refundAmount: {}",
                saga.getReturnRequestId(), saga.getOrderId(), saga.getRefundAmount());

        if (saga.getRefundAmount() == null || saga.getRefundAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("No refund amount set for return request {}, skipping settlement adjustment",
                    saga.getReturnRequestId());
            saga.setSettlementAdjustmentId("SKIP-NO-AMOUNT");
            return;
        }

        saga.getTransientMap().put("settlementAdjustmentRequestedAt", java.time.Instant.now().toString());

        eventPublisher.requestSettlementAdjustment(saga);

        log.info("Settlement adjustment requested for return request: {}, amount: {}",
                saga.getReturnRequestId(), saga.getRefundAmount());
    }
}
