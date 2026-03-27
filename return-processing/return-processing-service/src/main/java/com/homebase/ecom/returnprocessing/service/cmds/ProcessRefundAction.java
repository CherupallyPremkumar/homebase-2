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

/**
 * STM transition action: requests customer refund via the payment service.
 * Transition: SETTLEMENT_ADJUSTED -> REFUNDED
 *
 * Validates settlement is adjusted then publishes REFUND_PROCESSING_REQUESTED event.
 * Payment BC consumes this event and processes the refund.
 */
public class ProcessRefundAction extends AbstractSTMTransitionAction<ReturnProcessingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(ProcessRefundAction.class);

    private final ReturnProcessingEventPublisherPort eventPublisher;

    public ProcessRefundAction(ReturnProcessingEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void transitionTo(ReturnProcessingSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Requesting refund for return request: {}, order: {}, amount: {}",
                saga.getReturnRequestId(), saga.getOrderId(), saga.getRefundAmount());

        if (saga.getSettlementAdjustmentId() == null) {
            String msg = "Cannot process refund: settlement not yet adjusted for return request " + saga.getReturnRequestId();
            saga.setErrorMessage(msg);
            throw new RuntimeException(msg);
        }

        saga.getTransientMap().put("refundRequestedAt", java.time.Instant.now().toString());

        eventPublisher.requestRefundProcessing(saga);

        log.info("Refund processing requested for return request: {}, amount: {}",
                saga.getReturnRequestId(), saga.getRefundAmount());
    }
}
