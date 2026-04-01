package com.homebase.ecom.order.infrastructure.persistence.adapter;

import com.homebase.ecom.order.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, String> {
}
