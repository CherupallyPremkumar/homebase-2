package com.homebase.ecom.onboarding.infrastructure.persistence.adapter;

import java.util.Optional;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.port.OnboardingSagaRepository;
import com.homebase.ecom.onboarding.infrastructure.persistence.entity.OnboardingSagaEntity;
import com.homebase.ecom.onboarding.infrastructure.persistence.mapper.OnboardingSagaMapper;

public class OnboardingSagaRepositoryImpl implements OnboardingSagaRepository {

    private final OnboardingSagaJpaRepository jpaRepository;
    private final OnboardingSagaMapper mapper;

    public OnboardingSagaRepositoryImpl(OnboardingSagaJpaRepository jpaRepository, OnboardingSagaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<OnboardingSaga> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<OnboardingSaga> findBySupplierId(String supplierId) {
        return jpaRepository.findBySupplierId(supplierId).map(mapper::toModel);
    }

    @Override
    public void save(OnboardingSaga saga) {
        OnboardingSagaEntity entity = mapper.toEntity(saga);
        jpaRepository.save(entity);
        saga.setId(entity.getId());
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
