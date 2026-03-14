package com.homebase.ecom.inventory.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.port.InventoryItemRepository;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.InventoryItemEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.mapper.InventoryItemMapper;

public class InventoryItemRepositoryImpl implements InventoryItemRepository {

    private final InventoryItemJpaRepository jpaRepository;
    private final InventoryItemMapper mapper;

    public InventoryItemRepositoryImpl(InventoryItemJpaRepository jpaRepository, InventoryItemMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<InventoryItem> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<InventoryItem> findByProductId(String productId) {
        return jpaRepository.findByProductId(productId).map(mapper::toModel);
    }

    @Override
    public List<InventoryItem> findByOrderIdInReservations(String orderId) {
        return jpaRepository.findByOrderIdInReservations(orderId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByOrderIdInReservations(String orderId) {
        return jpaRepository.existsByOrderIdInReservations(orderId);
    }

    @Override
    public void save(InventoryItem inventoryItem) {
        InventoryItemEntity entity = mapper.toEntity(inventoryItem);
        jpaRepository.save(entity);
        inventoryItem.setId(entity.getId()); // Ensure ID is propagated back if it was generated
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
