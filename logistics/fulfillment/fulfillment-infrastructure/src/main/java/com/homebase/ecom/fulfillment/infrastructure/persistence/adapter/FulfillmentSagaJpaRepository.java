package com.homebase.ecom.fulfillment.infrastructure.persistence.adapter;

import com.homebase.ecom.fulfillment.infrastructure.persistence.entity.FulfillmentSagaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FulfillmentSagaJpaRepository extends JpaRepository<FulfillmentSagaEntity, String> {

    Optional<FulfillmentSagaEntity> findByOrderId(String orderId);
}
