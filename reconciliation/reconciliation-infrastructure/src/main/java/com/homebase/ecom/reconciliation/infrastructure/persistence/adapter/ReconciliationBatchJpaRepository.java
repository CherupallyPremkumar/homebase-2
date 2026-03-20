package com.homebase.ecom.reconciliation.infrastructure.persistence.adapter;

import com.homebase.ecom.reconciliation.infrastructure.persistence.entity.ReconciliationBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReconciliationBatchJpaRepository extends JpaRepository<ReconciliationBatchEntity, String> {

    List<ReconciliationBatchEntity> findByBatchDate(LocalDate batchDate);

    List<ReconciliationBatchEntity> findByGatewayType(String gatewayType);
}
