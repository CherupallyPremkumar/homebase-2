package com.homebase.ecom.order.infrastructure.persistence;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.infrastructure.persistence.adapter.OrderJpaRepository;
import com.homebase.ecom.order.infrastructure.persistence.entity.OrderEntity;
import com.homebase.ecom.order.infrastructure.persistence.mapper.OrderMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

public class ChenileOrderEntityStore extends ChenileJpaEntityStore<Order, OrderEntity> {

    public ChenileOrderEntityStore(OrderJpaRepository repository, OrderMapper mapper) {
        super(repository, (entity) -> mapper.toModel(entity), (model) -> mapper.toEntity(model));
    }
}
