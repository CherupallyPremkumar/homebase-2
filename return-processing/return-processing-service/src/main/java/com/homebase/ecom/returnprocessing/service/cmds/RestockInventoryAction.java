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
 * STM transition action: requests inventory restock for the returned item.
 * Transition: ITEM_RECEIVED -> INVENTORY_RESTOCKED
 *
 * Validates inspection status then publishes INVENTORY_RESTOCK_REQUESTED event.
 * Inventory BC consumes this event and restocks the item.
 */
public class RestockInventoryAction extends AbstractSTMTransitionAction<ReturnProcessingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(RestockInventoryAction.class);

    private final ReturnProcessingEventPublisherPort eventPublisher;

    public RestockInventoryAction(ReturnProcessingEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void transitionTo(ReturnProcessingSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Requesting inventory restock for return request: {}, orderItem: {}",
                saga.getReturnRequestId(), saga.getOrderItemId());

        String inspectionStatus = (String) saga.getTransientMap().get("warehouseInspectionStatus");
        if (!"PASSED".equals(inspectionStatus)) {
            String msg = "Cannot restock: item failed warehouse inspection for return request " + saga.getReturnRequestId();
            saga.setErrorMessage(msg);
            throw new RuntimeException(msg);
        }

        saga.getTransientMap().put("restockRequestedAt", java.time.Instant.now().toString());

        eventPublisher.requestInventoryRestock(saga);

        log.info("Inventory restock requested for return request: {}",
                saga.getReturnRequestId());
    }
}
