package com.homebase.ecom.shipping.infrastructure.persistence.adapter;

import com.homebase.ecom.shipping.infrastructure.persistence.entity.ShippingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingJpaRepository extends JpaRepository<ShippingEntity, String> {
}
