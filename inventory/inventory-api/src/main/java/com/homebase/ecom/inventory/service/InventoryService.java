package com.homebase.ecom.inventory.service;

import com.homebase.ecom.inventory.dto.InventoryDto;
import java.util.Map;

/**
 * Interface for inventory-related operations, to be called across modules.
 */
public interface InventoryService {
    /**
     * Reserves stock for an order/cart.
     */
    void reserveForOrder(String orderId, Map<String, Integer> items) throws Exception;

    /**
     * Commits the reserved stock (after payment).
     */
    void commit(String orderId);

    /**
     * Releases the reserved stock (after failure or abandonment).
     */
    void release(String orderId);

    /**
     * Retrieves inventory details for a product.
     */
    InventoryDto getInventory(String productId);
}
