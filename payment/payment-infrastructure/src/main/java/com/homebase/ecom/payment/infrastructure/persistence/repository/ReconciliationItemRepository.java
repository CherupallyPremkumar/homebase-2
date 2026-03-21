package com.homebase.ecom.payment.infrastructure.persistence.repository;

import com.homebase.ecom.payment.infrastructure.persistence.entity.ReconciliationItem;
import com.homebase.ecom.payment.infrastructure.enums.ReconciliationItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReconciliationItemRepository extends JpaRepository<ReconciliationItem, String> {

    List<ReconciliationItem> findByRunIdAndStatus(String runId, ReconciliationItemStatus status);

    List<ReconciliationItem> findByRunId(String runId);
}
