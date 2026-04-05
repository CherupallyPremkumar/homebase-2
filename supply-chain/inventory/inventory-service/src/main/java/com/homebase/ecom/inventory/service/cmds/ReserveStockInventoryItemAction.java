package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.service.validator.InventoryItemPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.ReserveStockInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for reserving stock against an order.
 * Validates that sufficient stock is available, checks policy constraints,
 * then creates a reservation with a configurable TTL.
 */
public class ReserveStockInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, ReserveStockInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(ReserveStockInventoryItemAction.class);

    
    private final InventoryItemPolicyValidator policyValidator;

    public ReserveStockInventoryItemAction(InventoryItemPolicyValidator policyValidator) {
        this.policyValidator = policyValidator;
    }

    @Override
    public void transitionTo(InventoryItem inventory, ReserveStockInventoryPayload payload, State startState,
            String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int requestedQty = payload.getQuantity();

        // Policy enforcement: max per order and max active reservations
        policyValidator.validateReservation(inventory, requestedQty);

        // Use domain method to handle reservation logic
        inventory.reserveStock(requestedQty, payload.getOrderId(),
            null, // sessionId
            policyValidator.getReservationTtlMinutes());

        if (inventory.isLowStock()) {
            inventory.getTransientMap().put("lowStockAlert", true);
        }

        log.info("Stock reserved for productId={}, orderId={}, qty={}, remainingAvailable={}",
                inventory.getProductId(), payload.getOrderId(), requestedQty, inventory.getAvailableQuantity());

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("reservedOrderId", payload.getOrderId());
        inventory.getTransientMap().put("reservedQuantity", requestedQty);
    }
}
