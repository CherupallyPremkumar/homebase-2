package com.homebase.ecom.checkout.infrastructure.integration;

import com.homebase.ecom.checkout.domain.port.InventoryReservePort;
import com.homebase.ecom.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Driven adapter: reserves/releases inventory via Inventory service.
 * Delegates to inventory-client's InventoryService proxy (local in monolith, HTTP in microservices).
 */
public class InventoryReserveAdapter implements InventoryReservePort {

    private static final Logger log = LoggerFactory.getLogger(InventoryReserveAdapter.class);

    private final InventoryService inventoryService;

    public InventoryReserveAdapter(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    public void reserve(String checkoutId, Map<String, Integer> variantQuantities) {
        log.info("Reserving inventory for checkout={}, variants={}", checkoutId, variantQuantities);
        // TODO: delegate to inventory-client reserve endpoint
        try {
            inventoryService.reserveForOrder(checkoutId, variantQuantities);
            log.info("Inventory reserved successfully for checkout={}", checkoutId);
        } catch (Exception e) {
            log.error("Failed to reserve inventory for checkout={}: {}", checkoutId, e.getMessage());
            throw new RuntimeException("Inventory reservation failed for checkout=" + checkoutId, e);
        }
    }

    @Override
    public void release(String checkoutId) {
        log.info("Releasing inventory reservation for checkout={}", checkoutId);
        // TODO: delegate to inventory-client release endpoint
        try {
            inventoryService.release(checkoutId);
            log.info("Inventory released successfully for checkout={}", checkoutId);
        } catch (Exception e) {
            log.error("Failed to release inventory for checkout={}: {}", checkoutId, e.getMessage());
            throw new RuntimeException("Inventory release failed for checkout=" + checkoutId, e);
        }
    }
}
