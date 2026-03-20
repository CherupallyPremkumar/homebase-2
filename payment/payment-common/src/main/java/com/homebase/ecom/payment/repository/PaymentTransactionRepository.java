package com.homebase.ecom.payment.repository;

import com.homebase.ecom.payment.domain.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, String> {

    List<PaymentTransaction> findByOrderId(String orderId);

    Optional<PaymentTransaction> findByGatewayChargeId(String gatewayChargeId);

    Optional<PaymentTransaction> findByGatewayTransactionId(String gatewayTransactionId);

    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.status = 'SUCCEEDED' " +
            "AND pt.createdTime >= :start AND pt.createdTime <= :end")
    List<PaymentTransaction> findSucceededInRange(
            @Param("start") java.util.Date start,
            @Param("end") java.util.Date end);

    List<PaymentTransaction> findByStatus(String status);

    org.springframework.data.domain.Page<PaymentTransaction> findByStatus(String status,
            org.springframework.data.domain.Pageable pageable);
}
