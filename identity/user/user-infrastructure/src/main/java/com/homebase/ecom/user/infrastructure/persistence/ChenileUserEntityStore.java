package com.homebase.ecom.user.infrastructure.persistence;

import com.homebase.ecom.user.domain.model.User;
import com.homebase.ecom.user.infrastructure.persistence.adapter.UserJpaRepository;
import com.homebase.ecom.user.infrastructure.persistence.entity.UserJpaEntity;
import com.homebase.ecom.user.infrastructure.persistence.mapper.UserMapper;
import com.homebase.ecom.core.jpa.ChenileJpaEntityStore;

/**
 * Bridges Chenile STM's EntityStore with JPA persistence for User.
 * Uses ChenileJpaEntityStore which properly handles:
 * - Optimistic locking via @Version (loads existing entity before update)
 * - Merge function to copy fields from updated entity to managed entity
 * - ID propagation back to domain model after persist
 */
public class ChenileUserEntityStore extends ChenileJpaEntityStore<User, UserJpaEntity> {

    public ChenileUserEntityStore(UserJpaRepository repository, UserMapper mapper) {
        super(repository,
                entity -> mapper.toModel(entity),
                model -> mapper.toEntity(model),
                (existing, updated) -> mapper.mergeEntity(existing, updated));
    }
}
