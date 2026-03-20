package com.homebase.ecom.returnprocessing.infrastructure.persistence;

import com.homebase.ecom.returnprocessing.infrastructure.persistence.mapper.ReturnProcessingSagaMapper;
import com.homebase.ecom.returnprocessing.infrastructure.persistence.entity.ReturnProcessingSagaEntity;
import com.homebase.ecom.returnprocessing.infrastructure.persistence.repository.ReturnProcessingSagaJpaRepository;
import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

/**
 * Chenile EntityStore bridge for return processing saga.
 * Uses ChenileJpaEntityStore which properly handles:
 *  - ID generation (copies generated ID back to domain model)
 *  - Version field for optimistic locking
 *  - Entity merge on update path
 * Wired as @Bean in ReturnProcessingConfiguration.
 */
public class ChenileReturnProcessingEntityStore extends ChenileJpaEntityStore<ReturnProcessingSaga, ReturnProcessingSagaEntity> {

    public ChenileReturnProcessingEntityStore(ReturnProcessingSagaJpaRepository repository,
                                               ReturnProcessingSagaMapper mapper) {
        super(repository,
              entity -> mapper.toModel(entity),
              model -> mapper.toEntity(model),
              (existing, incoming) -> mapper.mergeJpaEntity(existing, incoming));
    }
}
