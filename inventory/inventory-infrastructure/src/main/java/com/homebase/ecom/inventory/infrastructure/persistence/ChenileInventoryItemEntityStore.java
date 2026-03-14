package com.homebase.ecom.inventory.infrastructure.persistence;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.infrastructure.persistence.adapter.InventoryItemJpaRepository;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.InventoryItemEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.mapper.InventoryItemMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

public class ChenileInventoryItemEntityStore extends ChenileJpaEntityStore<InventoryItem, InventoryItemEntity> {

    public ChenileInventoryItemEntityStore(InventoryItemJpaRepository repository, InventoryItemMapper mapper) {
        super(repository, (entity) -> mapper.toModel(entity), (model) -> mapper.toEntity(model));
    }
}
