package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.ReturnCompletedInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action when a return to supplier is completed and acknowledged.
 */
public class ReturnCompletedInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, ReturnCompletedInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(ReturnCompletedInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            ReturnCompletedInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Return completed for productId={}, supplierAckRef={}",
                inventory.getProductId(),
                payload.getSupplierAcknowledgementRef() != null ? payload.getSupplierAcknowledgementRef() : "N/A");

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("returnCompleted", true);
    }
}
