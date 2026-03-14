package com.homebase.ecom.inventory.service.impl;

import com.homebase.ecom.inventory.configuration.repository.InventoryQueryRepository;
import com.homebase.ecom.inventory.dto.InventoryDto;
import com.homebase.ecom.inventory.dto.ReserveStockInventoryPayload;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.port.InventoryItemRepository;
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
 * Implementation of InventoryService that coordinates with the Chenile STM.
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Autowired
    @Qualifier("_inventoryStateEntityService_")
    private StateEntityServiceImpl<InventoryItem> inventoryStateEntityService;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private InventoryQueryRepository inventoryQueryRepository;

    @Override
    @Transactional
    public void reserveForOrder(String orderId, Map<String, Integer> items) {
        if (inventoryItemRepository.existsByOrderIdInReservations(orderId)) {
            log.info("Inventory already reserved for order {}. Skipping.", orderId);
            return;
        }

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String productId = entry.getKey();
            Integer quantity = entry.getValue();

        InventoryDto inventoryDetails = inventoryQueryRepository.getByProductId(productId);
            if (inventoryDetails == null) {
                throw new RuntimeException("Inventory not found for product: " + productId);
            }

            ReserveStockInventoryPayload payload = new ReserveStockInventoryPayload();
            payload.setOrderId(orderId);
            payload.setQuantity(quantity);

            inventoryStateEntityService.processById(inventoryDetails.getId(), "reserveStock", payload);
        }
    }

    @Override
    @Transactional
    public void commit(String orderId) {
        List<InventoryItem> items = inventoryItemRepository.findByOrderIdInReservations(orderId);

        for (InventoryItem inventory : items) {
            // Internal domain logic handles quantity adjustment and reservation confirmation
            inventory.confirmReservation(orderId);

            if (inventory.getQuantity() == 0) {
                inventoryStateEntityService.processById(inventory.getId(), "soldAllReserved", null);
            } else {
                inventoryItemRepository.save(inventory);
            }
        }
    }

    @Override
    @Transactional
    public void release(String orderId) {
        List<InventoryItem> items = inventoryItemRepository.findByOrderIdInReservations(orderId);

        for (InventoryItem inventory : items) {
            // Internal domain logic handles reservation release
            inventory.releaseReservation(orderId);

            if (inventory.getReservedQuantity() <= 0 && inventory.getCurrentState() != null &&
                    "PARTIALLY_RESERVED".equals(inventory.getCurrentState().getStateId())) {
                inventoryStateEntityService.processById(inventory.getId(), "releaseReservedStock", null);
            } else {
                inventoryItemRepository.save(inventory);
            }
        }
    }

    @Override
    public InventoryDto getInventory(String productId) {
        return inventoryQueryRepository.getByProductId(productId);
    }
}
