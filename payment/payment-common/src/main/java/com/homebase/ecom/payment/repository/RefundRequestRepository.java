package com.homebase.ecom.payment.repository;

import com.homebase.ecom.payment.domain.RefundRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundRequestRepository extends JpaRepository<RefundRequest, String> {
    List<RefundRequest> findByStatus(String status);

    List<RefundRequest> findByOrderId(String orderId);
}
