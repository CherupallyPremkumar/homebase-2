package com.homebase.ecom.returnprocessing.service.cmds;

import com.homebase.ecom.returnprocessing.dto.InspectItemPayload;
import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM transition action: inspects the returned item at the warehouse.
 * Sets itemCondition on the transient map for the CHECK_ITEM_CONDITION auto-state.
 * Transition: ITEM_RECEIVED -> CHECK_ITEM_CONDITION (auto-state)
 */
public class InspectItemAction extends AbstractSTMTransitionAction<ReturnProcessingSaga, InspectItemPayload> {

    private static final Logger log = LoggerFactory.getLogger(InspectItemAction.class);

    @Override
    public void transitionTo(ReturnProcessingSaga saga, InspectItemPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Inspecting returned item for return request: {}, orderItem: {}",
                saga.getReturnRequestId(), saga.getOrderItemId());

        String itemCondition = "GOOD";
        if (payload != null && payload.getItemCondition() != null) {
            itemCondition = payload.getItemCondition();
        }

        // Set on transient map for OGNL evaluation in CHECK_ITEM_CONDITION auto-state
        saga.getTransientMap().put("itemCondition", itemCondition);

        if (payload != null && payload.getDamageDescription() != null) {
            saga.getTransientMap().put("damageDescription", payload.getDamageDescription());
        }

        saga.getTransientMap().put("inspectedAt", java.time.Instant.now().toString());
        saga.getTransientMap().put("resellable", payload != null && payload.isResellable());

        log.info("Item inspected for return request: {}, condition: {}",
                saga.getReturnRequestId(), itemCondition);
    }
}
