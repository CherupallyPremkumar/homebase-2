package com.homebase.ecom.offer.infrastructure.persistence;

import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.infrastructure.persistence.adapter.OfferJpaRepository;
import com.homebase.ecom.offer.infrastructure.persistence.entity.OfferEntity;
import com.homebase.ecom.offer.infrastructure.persistence.mapper.OfferMapper;
import com.homebase.ecom.core.jpa.ChenileJpaEntityStore;

/**
 * Bridges Chenile STM's EntityStore with JPA persistence for Offer.
 * Uses ChenileJpaEntityStore which properly handles:
 * - Optimistic locking via @Version (loads existing entity before update)
 * - ID propagation back to domain model after persist
 */
public class ChenileOfferEntityStore extends ChenileJpaEntityStore<Offer, OfferEntity> {

    public ChenileOfferEntityStore(OfferJpaRepository repository, OfferMapper mapper) {
        super(repository,
                entity -> mapper.toModel(entity),
                model -> mapper.toEntity(model),
                (existing, updated) -> {
                    // Merge updated fields onto existing JPA entity
                    // Convert updated entity back to domain model, then use mergeEntity
                    Offer incoming = mapper.toModel(updated);
                    mapper.mergeEntity(incoming, existing);
                });
    }
}
