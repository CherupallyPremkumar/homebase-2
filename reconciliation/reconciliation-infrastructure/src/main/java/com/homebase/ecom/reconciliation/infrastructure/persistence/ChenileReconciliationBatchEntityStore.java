package com.homebase.ecom.reconciliation.infrastructure.persistence;

import com.homebase.ecom.reconciliation.model.ReconciliationBatch;
import com.homebase.ecom.reconciliation.infrastructure.persistence.entity.ReconciliationBatchEntity;
import com.homebase.ecom.reconciliation.infrastructure.persistence.adapter.ReconciliationBatchJpaRepository;
import com.homebase.ecom.reconciliation.infrastructure.persistence.mapper.ReconciliationBatchMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

public class ChenileReconciliationBatchEntityStore extends ChenileJpaEntityStore<ReconciliationBatch, ReconciliationBatchEntity> {

    public ChenileReconciliationBatchEntityStore(ReconciliationBatchJpaRepository repository, ReconciliationBatchMapper mapper) {
        super(repository,
                entity -> mapper.toModel(entity),
                model -> mapper.toEntity(model),
                (existing, updated) -> mapper.mergeEntity(existing, updated));
    }
}
