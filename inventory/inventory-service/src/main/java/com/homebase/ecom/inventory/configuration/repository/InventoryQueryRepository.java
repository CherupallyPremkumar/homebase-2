package com.homebase.ecom.inventory.configuration.repository;

import com.homebase.ecom.inventory.dto.InventoryDto;
import org.chenile.query.repository.ChenileRepository;

/**
 * Repository interface for fetching Inventory details.
 * Bypasses the need for a manual query adapter.
 */
public interface InventoryQueryRepository extends ChenileRepository<InventoryDto> {
    InventoryDto getByProductId(String productId);
}
