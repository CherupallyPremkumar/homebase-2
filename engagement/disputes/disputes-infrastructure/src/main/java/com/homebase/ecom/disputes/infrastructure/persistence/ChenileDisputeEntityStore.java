package com.homebase.ecom.disputes.infrastructure.persistence;

import com.homebase.ecom.disputes.model.Dispute;
import com.homebase.ecom.disputes.infrastructure.persistence.adapter.DisputeJpaRepository;
import com.homebase.ecom.disputes.infrastructure.persistence.entity.DisputeEntity;
import com.homebase.ecom.disputes.infrastructure.persistence.mapper.DisputeMapper;
import com.homebase.ecom.core.jpa.ChenileJpaEntityStore;

/**
 * Bridges Chenile STM's EntityStore with JPA persistence for Dispute.
 * Uses ChenileJpaEntityStore which properly handles:
 * - Optimistic locking via @Version (loads existing entity before update)
 * - Merge function to copy fields from updated entity to managed entity
 * - ID propagation back to domain model after persist
 */
public class ChenileDisputeEntityStore extends ChenileJpaEntityStore<Dispute, DisputeEntity> {

    public ChenileDisputeEntityStore(DisputeJpaRepository repository, DisputeMapper mapper) {
        super(repository,
                entity -> mapper.toModel(entity),
                model -> mapper.toEntity(model),
                (existing, updated) -> mapper.mergeEntity(existing, updated));
    }
}
