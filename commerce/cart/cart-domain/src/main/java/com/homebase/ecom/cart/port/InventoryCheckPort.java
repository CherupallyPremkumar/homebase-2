package com.homebase.ecom.cart.port;

/**
 * Domain port for checking inventory availability.
 * Infrastructure adapter calls the inventory service synchronously.
 */
public interface InventoryCheckPort {

    /**
     * Returns true if the requested quantity is available for the given variant.
     * Inventory is tracked at variant level (every product has at least a default variant).
     */
    boolean isAvailable(String variantId, int quantity);

    /**
     * Returns the available quantity for the given variant.
     */
    int getAvailableQuantity(String variantId);
}
