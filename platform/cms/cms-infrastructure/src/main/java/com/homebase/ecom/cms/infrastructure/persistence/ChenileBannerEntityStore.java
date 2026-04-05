package com.homebase.ecom.cms.infrastructure.persistence;

import com.homebase.ecom.cms.model.Banner;
import com.homebase.ecom.cms.infrastructure.persistence.adapter.BannerJpaRepository;
import com.homebase.ecom.cms.infrastructure.persistence.entity.BannerEntity;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;
import com.homebase.ecom.core.jpa.ChenileJpaEntityStore;

/**
 * Bridges Chenile STM's EntityStore with JPA persistence for Banner.
 * Uses ChenileJpaEntityStore which properly handles:
 * - Optimistic locking via @Version (loads existing entity before update)
 * - Merge function to copy fields from updated entity to managed entity
 * - ID propagation back to domain model after persist
 */
public class ChenileBannerEntityStore extends ChenileJpaEntityStore<Banner, BannerEntity> {

    public ChenileBannerEntityStore(BannerJpaRepository repository, CmsMapper mapper) {
        super(repository,
                entity -> mapper.toModel(entity),
                model -> mapper.toEntity(model),
                (existing, updated) -> mapper.mergeBannerEntity(existing, updated));
    }
}
