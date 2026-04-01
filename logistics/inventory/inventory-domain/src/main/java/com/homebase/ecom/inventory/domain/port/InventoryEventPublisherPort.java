package com.homebase.ecom.inventory.domain.port;

import com.homebase.ecom.inventory.domain.model.InventoryItem;

/**
 * Outbound port for publishing inventory domain events.
 * Infrastructure layer provides the Kafka-backed implementation.
 * No @Component -- wired explicitly via @Bean.
 */
public interface InventoryEventPublisherPort {

    /**
     * Publish damage detected event (warehouse damage or partial damage).
     *
     * @param item           the inventory item
     * @param damagedQuantity number of damaged units
     * @param severeDamage   whether the damage is severe (partial damage only)
     */
    void publishDamageDetected(InventoryItem item, int damagedQuantity, boolean severeDamage);

    /**
     * Publish stock rejected event to notify supplier management.
     *
     * @param item   the inventory item
     * @param reason rejection reason
     */
    void publishStockRejected(InventoryItem item, String reason);

    /**
     * Publish low stock alert when available quantity falls below threshold.
     *
     * @param item the inventory item with low stock
     */
    void publishLowStockAlert(InventoryItem item);

    /**
     * Publish stock discarded event for financial write-off tracking.
     *
     * @param item the discarded inventory item
     */
    void publishStockDiscarded(InventoryItem item);

    /**
     * Publish restock arrived event when new stock arrives from out-of-stock state.
     *
     * @param item           the inventory item
     * @param restockQuantity number of restocked units
     */
    void publishRestockArrived(InventoryItem item, int restockQuantity);

    /**
     * Publish stock depleted event when inventory reaches zero.
     *
     * @param item the depleted inventory item
     */
    void publishStockDepleted(InventoryItem item);

    /**
     * Publish stock returned to supplier event for settlement adjustment.
     *
     * @param item the inventory item returned to supplier
     */
    void publishStockReturnedToSupplier(InventoryItem item);
}
