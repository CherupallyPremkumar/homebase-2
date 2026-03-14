package com.homebase.ecom.onboarding.infrastructure.persistence;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.infrastructure.persistence.adapter.OnboardingSagaJpaRepository;
import com.homebase.ecom.onboarding.infrastructure.persistence.entity.OnboardingSagaEntity;
import com.homebase.ecom.onboarding.infrastructure.persistence.mapper.OnboardingSagaMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

public class ChenileOnboardingSagaEntityStore extends ChenileJpaEntityStore<OnboardingSaga, OnboardingSagaEntity> {

    public ChenileOnboardingSagaEntityStore(OnboardingSagaJpaRepository repository, OnboardingSagaMapper mapper) {
        super(repository, (entity) -> mapper.toModel(entity), (model) -> mapper.toEntity(model));
    }
}
