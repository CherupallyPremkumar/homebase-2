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
 * STM transition action: requests a return pickup via the shipping service.
 * Transition: INITIATED -> PICKUP_SCHEDULED
 *
 * Publishes a PICKUP_SCHEDULING_REQUESTED event via the event publisher port.
 * Shipping BC consumes this event and schedules the pickup.
 */
public class SchedulePickupAction extends AbstractSTMTransitionAction<ReturnProcessingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(SchedulePickupAction.class);

    private final ReturnProcessingEventPublisherPort eventPublisher;

    public SchedulePickupAction(ReturnProcessingEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void transitionTo(ReturnProcessingSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Requesting pickup scheduling for return request: {}, order: {}",
                saga.getReturnRequestId(), saga.getOrderId());

        saga.getTransientMap().put("pickupRequestedAt", java.time.Instant.now().toString());
        saga.getTransientMap().put("returnShipmentStatus", "PICKUP_REQUESTED");

        eventPublisher.requestPickupScheduling(saga);

        log.info("Pickup scheduling requested for return request: {}",
                saga.getReturnRequestId());
    }
}
