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
 * STM transition action: schedules a return pickup via the shipping service.
 * Transition: INITIATED -> PICKUP_SCHEDULED
 */
public class SchedulePickupAction extends AbstractSTMTransitionAction<ReturnProcessingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(SchedulePickupAction.class);

    @Override
    public void transitionTo(ReturnProcessingSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Scheduling pickup for return request: {}, order: {}",
                saga.getReturnRequestId(), saga.getOrderId());

        // Create a return shipment record for carrier pickup
        String shipmentId = "RET-SHP-" + saga.getReturnRequestId() + "-" + System.currentTimeMillis();
        saga.setShipmentId(shipmentId);

        saga.getTransientMap().put("pickupScheduledAt", java.time.Instant.now().toString());
        saga.getTransientMap().put("returnShipmentStatus", "PICKUP_PENDING");

        log.info("Pickup scheduled for return request: {}, shipmentId: {}",
                saga.getReturnRequestId(), shipmentId);
    }
}
