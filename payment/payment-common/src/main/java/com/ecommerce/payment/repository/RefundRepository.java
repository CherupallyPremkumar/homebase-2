package com.ecommerce.payment.repository;

import com.ecommerce.payment.domain.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, String> {

    List<Refund> findByPaymentTransactionId(String paymentTransactionId);

    Optional<Refund> findByGatewayRefundId(String gatewayRefundId);

    Optional<Refund> findByGatewayTypeAndGatewayRefundId(String gatewayType, String gatewayRefundId);
}
