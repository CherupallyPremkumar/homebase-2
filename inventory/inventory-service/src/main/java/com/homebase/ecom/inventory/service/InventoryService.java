package com.homebase.ecom.inventory.service;

import java.util.Map;

/**
 * Service for managing inventory stock through transitions.
 * Wraps the underlying state machine for higher-level operations.
 */
public interface InventoryService {
    /**
     * Reserves stock for a given order or cart.
     * 
     * @param orderId Identifier for the reservation (order ID or cart ID).
     * @param items   Map of productId to quantity.
     */
    void reserveForOrder(String orderId, Map<String, Integer> items);

    /**
     * Commits a previously reserved stock (e.g., after successful payment).
     * 
     * @param orderId Identifier for the reservation.
     */
    void commit(String orderId);

    /**
     * Releases a previously reserved stock (e.g., after cancellation or
     * abandonment).
     *
     * @param orderId Identifier for the reservation.
     */
    void release(String orderId);

    /**
     * Retrieves inventory details for a product.
     *
     * @param productId the product identifier.
     * @return inventory details DTO.
     */
    com.homebase.ecom.inventory.dto.InventoryDto getInventory(String productId);
}
