package com.homebase.ecom.order.infrastructure.persistence;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.infrastructure.persistence.adapter.OrderJpaRepository;
import com.homebase.ecom.order.infrastructure.persistence.entity.OrderEntity;
import com.homebase.ecom.order.infrastructure.persistence.mapper.OrderMapper;
import com.homebase.ecom.core.jpa.ChenileJpaEntityStore;

/**
 * Bridges Chenile STM's EntityStore with JPA persistence for Order.
 * Uses ChenileJpaEntityStore which properly handles:
 * - Optimistic locking via @Version (loads existing entity before update)
 * - Merge function to copy fields from updated entity to managed entity
 * - ID propagation back to domain model after persist
 */
public class ChenileOrderEntityStore extends ChenileJpaEntityStore<Order, OrderEntity> {

    public ChenileOrderEntityStore(OrderJpaRepository repository, OrderMapper mapper) {
        super(repository,
                entity -> mapper.toModel(entity),
                model -> mapper.toEntity(model),
                (existing, updated) -> mapper.mergeJpaEntity(existing, updated));
    }
}
