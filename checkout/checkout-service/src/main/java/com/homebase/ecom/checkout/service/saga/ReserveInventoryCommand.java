package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.checkout.model.CheckoutItem;
import com.homebase.ecom.inventory.service.InventoryService;
import org.chenile.owiz.Command;
import org.chenile.workflow.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * OWIZ saga step 3: Reserve inventory for all checkout items.
 * Uses inventory-client's InventoryService proxy directly.
 */
public class ReserveInventoryCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(ReserveInventoryCommand.class);

    @Autowired(required = false)
    private InventoryService inventoryServiceClient;

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();
        if (inventoryServiceClient != null) {
            Map<String, Integer> items = checkout.getItems().stream()
                    .collect(Collectors.toMap(CheckoutItem::getVariantId, CheckoutItem::getQuantity));
            inventoryServiceClient.reserveForOrder(checkout.getId(), items);
            log.info("[CHECKOUT SAGA] Inventory reserved for checkout {}, {} items",
                    checkout.getId(), items.size());
        }
        checkout.setLastCompletedStep("reserveInventory");
    }
}
