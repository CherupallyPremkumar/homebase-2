package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.State;
import org.chenile.stm.action.STMAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exit action triggered when stock reaches RETURNED_TO_SUPPLIER state.
 * Logs the adjustment event for the settlement module to process.
 */
public class AdjustSupplierPaymentAction implements STMAction<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(AdjustSupplierPaymentAction.class);

    @Override
    public void execute(State fromState, State toState, InventoryItem inventory) throws Exception {
        log.info("Supplier payment adjustment triggered for inventory item: {}, productId={}",
                inventory.getId(), inventory.getProductId());
        // Mark for settlement module processing
        inventory.getTransientMap().put("supplierPaymentAdjustment", true);
    }
}
