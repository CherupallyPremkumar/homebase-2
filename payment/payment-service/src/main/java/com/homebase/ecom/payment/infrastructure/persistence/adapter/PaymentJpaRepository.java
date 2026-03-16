package com.homebase.ecom.payment.infrastructure.persistence.adapter;

import com.homebase.ecom.payment.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, String> {

    Optional<PaymentEntity> findByOrderId(String orderId);

    Optional<PaymentEntity> findByGatewayTransactionId(String gatewayTransactionId);
}
