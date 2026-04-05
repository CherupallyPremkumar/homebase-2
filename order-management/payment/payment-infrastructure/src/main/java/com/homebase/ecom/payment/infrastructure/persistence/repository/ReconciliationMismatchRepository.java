package com.homebase.ecom.payment.infrastructure.persistence.repository;

import com.homebase.ecom.payment.infrastructure.persistence.entity.ReconciliationMismatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReconciliationMismatchRepository extends JpaRepository<ReconciliationMismatch, String> {

    List<ReconciliationMismatch> findByResolvedFalse();

    List<ReconciliationMismatch> findByResolvedFalseOrderByCreatedTimeDesc();

    List<ReconciliationMismatch> findByResolvedFalseAndCreatedTimeAfterOrderByCreatedTimeDesc(java.util.Date createdTime);

    List<ReconciliationMismatch> findByOrderId(String orderId);

    Optional<ReconciliationMismatch> findByGatewayTypeAndProviderTransactionIdAndResolvedFalse(String gatewayType,
            String providerTransactionId);

    List<ReconciliationMismatch> findByGatewayTypeAndResolvedFalseOrderByCreatedTimeDesc(String gatewayType);
}
