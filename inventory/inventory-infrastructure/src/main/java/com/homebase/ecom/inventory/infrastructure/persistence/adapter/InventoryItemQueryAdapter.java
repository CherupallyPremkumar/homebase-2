package com.homebase.ecom.inventory.infrastructure.persistence.adapter;

import java.util.List;
import java.util.stream.Collectors;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.InventoryItemEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.mapper.InventoryItemMapper;

/**
 * Query adapter that wraps InventoryItemJpaRepository and InventoryItemMapper
 * to provide domain-level query and persistence methods.
 */
public class InventoryItemQueryAdapter {

    private final InventoryItemJpaRepository jpaRepository;
    private final InventoryItemMapper mapper;

    public InventoryItemQueryAdapter(InventoryItemJpaRepository jpaRepository, InventoryItemMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * Find an inventory item by its product ID.
     * @return the domain InventoryItem, or null if not found
     */
    public InventoryItem findByProductId(String productId) {
        return jpaRepository.findByProductId(productId)
                .map(mapper::toModel)
                .orElse(null);
    }

    /**
     * Find all inventory items that have an active reservation for the given order ID.
     */
    public List<InventoryItem> findByOrderIdInReservations(String orderId) {
        return jpaRepository.findByOrderIdInReservations(orderId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * Check whether any inventory item has an active reservation for the given order ID.
     */
    public boolean existsByOrderIdInReservations(String orderId) {
        return jpaRepository.existsByOrderIdInReservations(orderId);
    }

    /**
     * Save a domain InventoryItem by mapping it to an entity and persisting via JPA.
     * The generated ID (if any) is propagated back to the domain object.
     */
    public void save(InventoryItem item) {
        InventoryItemEntity entity = mapper.toEntity(item);
        jpaRepository.save(entity);
        item.setId(entity.getId());
    }
}
