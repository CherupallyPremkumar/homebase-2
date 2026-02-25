package com.ecommerce.payment.repository;

import com.ecommerce.payment.domain.ReconciliationItem;
import com.ecommerce.payment.domain.ReconciliationItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReconciliationItemRepository extends JpaRepository<ReconciliationItem, String> {

    List<ReconciliationItem> findByRunIdAndStatus(String runId, ReconciliationItemStatus status);

    List<ReconciliationItem> findByRunId(String runId);
}
