package com.homebase.ecom.cart.repository;

import java.util.List;

import org.chenile.query.annotation.ChenileParam;
import org.chenile.query.annotation.ChenileRepositoryDefinition;
import org.chenile.query.annotation.QueryName;
import org.chenile.query.repository.ChenileRepository;

import com.homebase.ecom.inventory.dto.InventoryDto;

/**
 * ChenileRepository for reading Inventory data via the inventory-query service.
 *
 * Query mapping (from inventory.json + inventory.xml):
 * "Inventory.getAll" → paginated list by productId / status / stateId
 * "Inventory.getByProductId" → single record lookup by productId
 *
 * Used by AbstractCartAction to perform stock validation before mutating the
 * cart.
 */
@ChenileRepositoryDefinition(entityClass = InventoryDto.class)
public interface InventoryRepository extends ChenileRepository<InventoryDto> {

    /**
     * Lookup inventory for a specific product (single record).
     * Maps to Inventory.getByProductId → name: "inventory" in inventory.json.
     */
    @QueryName("inventory")
    InventoryDto findById(@ChenileParam("productId") String productId);

    /**
     * Find all inventory records matching a productId (paginated).
     * Maps to Inventory.getAll → name: "inventories" in inventory.json.
     */
    @QueryName("inventories")
    List<InventoryDto> findByProductId(@ChenileParam("productId") String productId);

    /**
     * Find inventory records by state (e.g., LOW_STOCK, OUT_OF_STOCK).
     * Maps to Inventory.getAll → name: "inventories" in inventory.json.
     */
    @QueryName("inventories")
    List<InventoryDto> findByStateId(@ChenileParam("stateId") String stateId);
}
