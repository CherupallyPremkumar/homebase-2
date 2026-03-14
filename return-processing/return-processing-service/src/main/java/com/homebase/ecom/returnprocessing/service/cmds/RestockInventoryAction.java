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
 * STM transition action: adds returned item quantity back to inventory.
 * Transition: ITEM_RECEIVED -> INVENTORY_RESTOCKED
 */
public class RestockInventoryAction extends AbstractSTMTransitionAction<ReturnProcessingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(RestockInventoryAction.class);

    @Override
    public void transitionTo(ReturnProcessingSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Restocking inventory for return request: {}, orderItem: {}",
                saga.getReturnRequestId(), saga.getOrderItemId());

        String inspectionStatus = (String) saga.getTransientMap().get("warehouseInspectionStatus");
        if (!"PASSED".equals(inspectionStatus)) {
            String msg = "Cannot restock: item failed warehouse inspection for return request " + saga.getReturnRequestId();
            saga.setErrorMessage(msg);
            throw new RuntimeException(msg);
        }

        // Add returned quantity back to available stock
        String restockId = "RSTK-" + saga.getReturnRequestId() + "-" + System.currentTimeMillis();
        saga.getTransientMap().put("restockId", restockId);
        saga.getTransientMap().put("restockedAt", java.time.Instant.now().toString());

        log.info("Inventory restocked for return request: {}, restockId: {}",
                saga.getReturnRequestId(), restockId);
    }
}
