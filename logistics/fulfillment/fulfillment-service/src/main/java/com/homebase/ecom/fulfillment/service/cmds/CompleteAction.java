package com.homebase.ecom.fulfillment.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM transition action that marks the fulfillment as complete
 * and updates the order status to SHIPPED.
 * Replaces the old UpdateOrderFulfilledCommand OWIZ command.
 */
public class CompleteAction extends AbstractSTMTransitionAction<FulfillmentSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(CompleteAction.class);

    @Override
    public void transitionTo(FulfillmentSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm,
                             Transition transition) throws Exception {
        log.info("Completing fulfillment for order {}", saga.getOrderId());

        // Record completion metadata
        saga.getTransientMap().put("completedAt", java.time.Instant.now().toString());
        saga.getTransientMap().put("orderStatus", "SHIPPED");

        // Clear any transient error state
        saga.setErrorMessage(null);

        log.info("Fulfillment completed for order {}: shipmentId={}, tracking={}",
                saga.getOrderId(), saga.getShipmentId(), saga.getTrackingNumber());
    }
}
