package com.homebase.ecom.returnrequest.infrastructure.adapter;

import com.homebase.ecom.returnrequest.domain.port.InventoryPort;
import com.homebase.ecom.returnrequest.model.ReturnItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Adapter for the InventoryPort. In production, this publishes a Kafka event
 * to the inventory service to restock returned items.
 */
public class InventoryAdapter implements InventoryPort {

    private static final Logger log = LoggerFactory.getLogger(InventoryAdapter.class);

    @Override
    public void restockReturnedItems(String returnRequestId, List<ReturnItem> items, String warehouseId) {
        log.info("Restocking {} items from return request {} at warehouse {}",
                items.size(), returnRequestId, warehouseId);
        // In production: publish returnStock event to inventory.events topic for each item
    }
}
