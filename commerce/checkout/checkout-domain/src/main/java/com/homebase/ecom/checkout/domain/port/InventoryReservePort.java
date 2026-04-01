package com.homebase.ecom.checkout.domain.port;

import java.util.Map;

/**
 * Port for reserving/releasing inventory during checkout.
 * Adapter calls Inventory service's reserve/release endpoints.
 */
public interface InventoryReservePort {

    /**
     * Reserves inventory for the given items.
     * @param checkoutId used as the reservation reference
     * @param variantQuantities map of variantId → quantity to reserve
     */
    void reserve(String checkoutId, Map<String, Integer> variantQuantities);

    /**
     * Releases reserved inventory — used for compensation.
     */
    void release(String checkoutId);
}
