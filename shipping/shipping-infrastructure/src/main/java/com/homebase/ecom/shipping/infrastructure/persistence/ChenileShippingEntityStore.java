package com.homebase.ecom.shipping.infrastructure.persistence;

import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.infrastructure.persistence.adapter.ShippingJpaRepository;
import com.homebase.ecom.shipping.infrastructure.persistence.entity.ShippingEntity;
import com.homebase.ecom.shipping.infrastructure.persistence.mapper.ShippingMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

/**
 * Bridges Chenile STM's EntityStore with JPA persistence for Shipping.
 * Uses ChenileJpaEntityStore which properly handles:
 * - Optimistic locking via @Version (loads existing entity before update)
 * - Merge function to copy fields from updated entity to managed entity
 * - ID propagation back to domain model after persist
 */
public class ChenileShippingEntityStore extends ChenileJpaEntityStore<Shipping, ShippingEntity> {

    public ChenileShippingEntityStore(ShippingJpaRepository repository, ShippingMapper mapper) {
        super(repository,
                entity -> mapper.toModel(entity),
                model -> mapper.toEntity(model),
                (existing, updated) -> mapper.mergeEntity(existing, updated));
    }
}
