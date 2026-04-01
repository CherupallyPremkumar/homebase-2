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
 * STM transition action that confirms delivery of the order.
 * Transition: CUSTOMER_NOTIFIED -> COMPLETED
 * Triggered when the carrier confirms delivery or the customer acknowledges receipt.
 */
public class ConfirmDeliveryAction extends AbstractSTMTransitionAction<FulfillmentSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(ConfirmDeliveryAction.class);

    @Override
    public void transitionTo(FulfillmentSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm,
                             Transition transition) throws Exception {
        log.info("Confirming delivery for order {}", saga.getOrderId());

        saga.getTransientMap().put("deliveredAt", java.time.Instant.now().toString());
        saga.getTransientMap().put("shipmentStatus", "DELIVERED");
        saga.getTransientMap().put("orderStatus", "DELIVERED");
        saga.setErrorMessage(null);

        log.info("Delivery confirmed for order {}: shipmentId={}, tracking={}",
                saga.getOrderId(), saga.getShipmentId(), saga.getTrackingNumber());
    }
}
