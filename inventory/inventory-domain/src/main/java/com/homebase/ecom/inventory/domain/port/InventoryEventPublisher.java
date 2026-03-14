package com.homebase.ecom.inventory.domain.port;

import com.homebase.ecom.inventory.domain.model.InventoryItem;

/**
 * Domain port for publishing inventory-related domain events.
 * Implementations should send events via Kafka or other messaging infrastructure.
 */
public interface InventoryEventPublisher {

    /**
     * Published when stock has been reserved for an order.
     */
    void publishStockReserved(InventoryItem inventory, String orderId, int quantity);

    /**
     * Published when available stock drops below the low-stock threshold.
     */
    void publishLowStockAlert(InventoryItem inventory);

    /**
     * Published when inventory reaches OUT_OF_STOCK state.
     */
    void publishStockDepleted(InventoryItem inventory);

    /**
     * Published when damaged stock is found.
     */
    void publishDamageDetected(InventoryItem inventory, int damagedQuantity, boolean severe);

    /**
     * Published when stock is rejected during inspection.
     */
    void publishStockRejected(InventoryItem inventory, String reason);

    /**
     * Published when stock is returned to supplier.
     */
    void publishStockReturnedToSupplier(InventoryItem inventory);

    /**
     * Published when stock is discarded.
     */
    void publishStockDiscarded(InventoryItem inventory);

    /**
     * Published when new restock arrives.
     */
    void publishRestockArrived(InventoryItem inventory, int quantity);
}
