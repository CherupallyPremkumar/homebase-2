package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.domain.port.InventoryReservePort;
import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.checkout.model.CheckoutItem;
import org.chenile.owiz.Command;
import org.chenile.workflow.service.stmcmds.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * OWIZ saga step 3: Reserve inventory for all checkout items.
 * Uses InventoryReservePort (hexagonal).
 */
public class ReserveInventoryCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(ReserveInventoryCommand.class);

    private final InventoryReservePort inventoryReservePort;

    public ReserveInventoryCommand(InventoryReservePort inventoryReservePort) {
        this.inventoryReservePort = inventoryReservePort;
    }

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();

        Map<String, Integer> items = checkout.getItems().stream()
                .collect(Collectors.toMap(CheckoutItem::getVariantId, CheckoutItem::getQuantity));

        inventoryReservePort.reserve(checkout.getId(), items);

        checkout.setLastCompletedStep("reserveInventory");
        log.info("[CHECKOUT SAGA] Inventory reserved for checkout {}, {} items",
                checkout.getId(), items.size());
    }
}
