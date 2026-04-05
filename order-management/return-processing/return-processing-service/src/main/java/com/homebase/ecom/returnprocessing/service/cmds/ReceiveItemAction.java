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
 * STM transition action: marks the returned item as received at the warehouse.
 * Transition: PICKUP_SCHEDULED -> ITEM_RECEIVED
 */
public class ReceiveItemAction extends AbstractSTMTransitionAction<ReturnProcessingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(ReceiveItemAction.class);

    @Override
    public void transitionTo(ReturnProcessingSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Marking item as received for return request: {}, orderItem: {}",
                saga.getReturnRequestId(), saga.getOrderItemId());

        if (saga.getShipmentId() == null || saga.getShipmentId().isEmpty()) {
            String msg = "Cannot receive item without a return shipment for request: " + saga.getReturnRequestId();
            saga.setErrorMessage(msg);
            throw new RuntimeException(msg);
        }

        saga.getTransientMap().put("itemReceivedAt", java.time.Instant.now().toString());
        saga.getTransientMap().put("returnShipmentStatus", "DELIVERED");
        saga.getTransientMap().put("warehouseInspectionStatus", "PASSED");

        log.info("Item received at warehouse for return request: {}, orderItem: {}",
                saga.getReturnRequestId(), saga.getOrderItemId());
    }
}
