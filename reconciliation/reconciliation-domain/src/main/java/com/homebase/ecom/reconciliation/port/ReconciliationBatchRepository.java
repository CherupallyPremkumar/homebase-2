package com.homebase.ecom.reconciliation.port;

import com.homebase.ecom.reconciliation.model.ReconciliationBatch;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Driven port for reconciliation batch persistence.
 */
public interface ReconciliationBatchRepository {

    Optional<ReconciliationBatch> findById(String id);

    void save(ReconciliationBatch batch);

    void delete(String id);

    List<ReconciliationBatch> findByBatchDate(LocalDate batchDate);

    List<ReconciliationBatch> findByGatewayType(String gatewayType);
}
