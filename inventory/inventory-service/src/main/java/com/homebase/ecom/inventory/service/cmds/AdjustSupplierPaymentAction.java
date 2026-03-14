package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.chenile.workflow.param.MinimalPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exit action triggered when stock reaches RETURNED_TO_SUPPLIER state.
 * Logs the adjustment event for the settlement module to process.
 */
public class AdjustSupplierPaymentAction extends AbstractSTMTransitionAction<InventoryItem, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(AdjustSupplierPaymentAction.class);

    @Override
    public void transitionTo(InventoryItem inventory, MinimalPayload payload, State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Supplier payment adjustment triggered for inventory item: {}, productId={}",
                inventory.getId(), inventory.getProductId());
        // Mark for settlement module processing
        inventory.getTransientMap().put("supplierPaymentAdjustment", true);
    }
}
