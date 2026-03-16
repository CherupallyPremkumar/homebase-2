package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.SellAllStockInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for selling all available stock, moving inventory to OUT_OF_STOCK.
 */
public class SellAllStockInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, SellAllStockInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(SellAllStockInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            SellAllStockInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Use domain method to sell all stock
        inventory.sellAllStock();

        log.info("All stock sold for productId={}. Inventory is now OUT_OF_STOCK.", inventory.getProductId());

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("stockDepleted", true);
    }
}
