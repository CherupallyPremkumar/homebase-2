package com.homebase.ecom.order.infrastructure.persistence.adapter;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.domain.port.OrderRepository;
import com.homebase.ecom.order.infrastructure.persistence.entity.OrderEntity;
import com.homebase.ecom.order.infrastructure.persistence.mapper.OrderMapper;

import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderMapper mapper;

    public OrderRepositoryImpl(OrderJpaRepository jpaRepository, OrderMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Order> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public void save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        jpaRepository.save(entity);
        order.setId(entity.getId());
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
