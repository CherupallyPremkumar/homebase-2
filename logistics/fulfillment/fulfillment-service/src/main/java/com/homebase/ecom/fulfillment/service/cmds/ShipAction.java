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
 * STM transition action that marks the order as shipped.
 * Transition: SHIPMENT_CREATED -> SHIPPED
 * This is triggered when the warehouse confirms the package has been handed off to the carrier.
 */
public class ShipAction extends AbstractSTMTransitionAction<FulfillmentSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(ShipAction.class);

    @Override
    public void transitionTo(FulfillmentSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm,
                             Transition transition) throws Exception {
        log.info("Marking order {} as shipped with shipmentId={}, tracking={}",
                saga.getOrderId(), saga.getShipmentId(), saga.getTrackingNumber());

        if (saga.getShipmentId() == null || saga.getShipmentId().isEmpty()) {
            String msg = "Cannot ship order " + saga.getOrderId() + " without a shipment record";
            saga.setErrorMessage(msg);
            throw new RuntimeException(msg);
        }

        saga.getTransientMap().put("shippedAt", java.time.Instant.now().toString());
        saga.getTransientMap().put("shipmentStatus", "IN_TRANSIT");

        log.info("Order {} shipped successfully", saga.getOrderId());
    }
}
