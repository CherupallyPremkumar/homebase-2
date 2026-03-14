package com.homebase.ecom.inventory.domain.port;

import java.util.List;
import java.util.Optional;
import com.homebase.ecom.inventory.domain.model.InventoryItem;

public interface InventoryItemRepository {
    Optional<InventoryItem> findById(String id);
    Optional<InventoryItem> findByProductId(String productId);
    List<InventoryItem> findByOrderIdInReservations(String orderId);
    boolean existsByOrderIdInReservations(String orderId);
    void save(InventoryItem inventoryItem);
    void delete(String id);
}
