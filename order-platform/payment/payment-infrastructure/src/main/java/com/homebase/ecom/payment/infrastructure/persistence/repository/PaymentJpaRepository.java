package com.homebase.ecom.payment.infrastructure.persistence.repository;

import com.homebase.ecom.payment.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, String> {

    Optional<PaymentEntity> findByOrderId(String orderId);

    Optional<PaymentEntity> findByGatewayTransactionId(String gatewayTransactionId);
}
