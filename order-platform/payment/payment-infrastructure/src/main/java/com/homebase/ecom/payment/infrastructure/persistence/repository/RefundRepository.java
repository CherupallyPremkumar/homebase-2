package com.homebase.ecom.payment.infrastructure.persistence.repository;

import com.homebase.ecom.payment.infrastructure.persistence.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, String> {

    List<Refund> findByPaymentTransactionId(String paymentTransactionId);

    Optional<Refund> findByGatewayRefundId(String gatewayRefundId);

    Optional<Refund> findByGatewayTypeAndGatewayRefundId(String gatewayType, String gatewayRefundId);
}
