package com.homebase.ecom.inventory.service.impl;

import com.homebase.ecom.inventory.dto.InventoryDto;
import com.homebase.ecom.inventory.dto.ReleaseReservedStockInventoryPayload;
import com.homebase.ecom.inventory.dto.ReserveStockInventoryPayload;
import com.homebase.ecom.inventory.dto.SoldAllReservedInventoryPayload;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.infrastructure.persistence.adapter.InventoryItemQueryAdapter;
import com.homebase.ecom.inventory.service.InventoryService;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Thin orchestrator — all mutations go through STM processById.
 * No direct save() calls; EntityStore handles persistence.
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Autowired
    @Qualifier("_inventoryStateEntityService_")
    private StateEntityServiceImpl<InventoryItem> inventoryStateEntityService;

    @Autowired
    private InventoryItemQueryAdapter inventoryItemQueryAdapter;

    @Override
    @Transactional
    public void reserveForOrder(String orderId, Map<String, Integer> items) {
        if (inventoryItemQueryAdapter.existsByOrderIdInReservations(orderId)) {
            log.info("Inventory already reserved for order {}. Skipping.", orderId);
            return;
        }

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            InventoryItem inventoryItem = inventoryItemQueryAdapter.findByProductId(entry.getKey());
            if (inventoryItem == null) {
                throw new RuntimeException("Inventory not found for product: " + entry.getKey());
            }

            ReserveStockInventoryPayload payload = new ReserveStockInventoryPayload();
            payload.setOrderId(orderId);
            payload.setQuantity(entry.getValue());

            inventoryStateEntityService.processById(inventoryItem.getId(), "reserveStock", payload);
        }
    }

    @Override
    @Transactional
    public void commit(String orderId) {
        List<InventoryItem> items = inventoryItemQueryAdapter.findByOrderIdInReservations(orderId);

        for (InventoryItem inventory : items) {
            SoldAllReservedInventoryPayload payload = new SoldAllReservedInventoryPayload();
            payload.setOrderId(orderId);
            // STM action confirms reservation; CHECK_DEPLETION auto-state routes to OUT_OF_STOCK or IN_WAREHOUSE
            inventoryStateEntityService.processById(inventory.getId(), "soldAllReserved", payload);
        }
    }

    @Override
    @Transactional
    public void release(String orderId) {
        List<InventoryItem> items = inventoryItemQueryAdapter.findByOrderIdInReservations(orderId);

        for (InventoryItem inventory : items) {
            ReleaseReservedStockInventoryPayload payload = new ReleaseReservedStockInventoryPayload();
            payload.setOrderId(orderId);
            // STM action releases reservation; self-transition on IN_WAREHOUSE
            inventoryStateEntityService.processById(inventory.getId(), "releaseReservedStock", payload);
        }
    }

    @Override
    public InventoryDto getInventory(String productId) {
        InventoryItem item = inventoryItemQueryAdapter.findByProductId(productId);
        if (item == null) return null;
        InventoryDto dto = new InventoryDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        dto.setReserved(item.getReservedQuantity());
        dto.setStateId(item.getCurrentState() != null ? item.getCurrentState().getStateId() : null);
        return dto;
    }
}
