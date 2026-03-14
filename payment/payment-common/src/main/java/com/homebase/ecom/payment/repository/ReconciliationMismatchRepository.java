package com.homebase.ecom.payment.repository;

import com.homebase.ecom.payment.domain.ReconciliationMismatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReconciliationMismatchRepository extends JpaRepository<ReconciliationMismatch, String> {

    List<ReconciliationMismatch> findByResolvedFalse();

    List<ReconciliationMismatch> findByResolvedFalseOrderByCreatedAtDesc();

    List<ReconciliationMismatch> findByResolvedFalseAndCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime createdAt);

    List<ReconciliationMismatch> findByOrderId(String orderId);

    Optional<ReconciliationMismatch> findByGatewayTypeAndProviderTransactionIdAndResolvedFalse(String gatewayType,
            String providerTransactionId);

    List<ReconciliationMismatch> findByGatewayTypeAndResolvedFalseOrderByCreatedAtDesc(String gatewayType);
}
