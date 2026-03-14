package com.homebase.ecom.inventory.service.postSaveHooks;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.port.InventoryEventPublisher;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post save hook for PARTIALLY_RESERVED state.
 * Publishes StockReservedEvent and LowStockAlertEvent when thresholds are breached.
 */
public class PARTIALLY_RESERVEDInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(PARTIALLY_RESERVEDInventoryItemPostSaveHook.class);

    @Autowired(required = false)
    private InventoryEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        if (eventPublisher == null) return;

        // Publish stock reserved event
        String orderId = (String) map.get("reservedOrderId");
        Integer qty = (Integer) map.get("reservedQuantity");
        if (orderId != null && qty != null) {
            eventPublisher.publishStockReserved(inventory, orderId, qty);
            log.info("Published StockReservedEvent for productId={}, orderId={}", inventory.getProductId(), orderId);
        }

        // Check for low stock alert
        Boolean lowStockAlert = (Boolean) map.get("lowStockAlert");
        if (Boolean.TRUE.equals(lowStockAlert)) {
            eventPublisher.publishLowStockAlert(inventory);
            log.warn("Published LowStockAlertEvent for productId={}, available={}",
                    inventory.getProductId(), inventory.getAvailableQuantity());
        }
    }
}
