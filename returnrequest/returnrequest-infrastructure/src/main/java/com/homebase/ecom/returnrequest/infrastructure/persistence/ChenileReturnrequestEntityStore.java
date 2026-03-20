package com.homebase.ecom.returnrequest.infrastructure.persistence;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.infrastructure.persistence.adapter.ReturnrequestJpaRepository;
import com.homebase.ecom.returnrequest.infrastructure.persistence.entity.ReturnrequestEntity;
import com.homebase.ecom.returnrequest.infrastructure.persistence.mapper.ReturnrequestMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

/**
 * Bridges Chenile STM's EntityStore with JPA persistence for Returnrequest.
 * Uses ChenileJpaEntityStore which properly handles:
 * - Optimistic locking via @Version (loads existing entity before update)
 * - Merge function to copy fields from updated entity to managed entity
 * - ID propagation back to domain model after persist
 */
public class ChenileReturnrequestEntityStore extends ChenileJpaEntityStore<Returnrequest, ReturnrequestEntity> {

    public ChenileReturnrequestEntityStore(ReturnrequestJpaRepository repository, ReturnrequestMapper mapper) {
        super(repository,
                entity -> mapper.toModel(entity),
                model -> mapper.toEntity(model),
                (existing, updated) -> mapper.mergeEntity(existing, updated));
    }
}
