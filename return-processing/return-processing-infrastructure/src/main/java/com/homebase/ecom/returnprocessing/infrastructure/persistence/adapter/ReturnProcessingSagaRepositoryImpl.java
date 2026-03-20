package com.homebase.ecom.returnprocessing.infrastructure.persistence.adapter;

import com.homebase.ecom.returnprocessing.infrastructure.persistence.mapper.ReturnProcessingSagaMapper;
import com.homebase.ecom.returnprocessing.infrastructure.persistence.repository.ReturnProcessingSagaJpaRepository;
import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import com.homebase.ecom.returnprocessing.port.ReturnProcessingSagaRepository;

import java.util.Optional;

/**
 * Infrastructure adapter implementing the domain port for return processing saga persistence.
 * Wired as @Bean in ReturnProcessingConfiguration.
 */
public class ReturnProcessingSagaRepositoryImpl implements ReturnProcessingSagaRepository {

    private final ReturnProcessingSagaJpaRepository jpaRepository;
    private final ReturnProcessingSagaMapper mapper;

    public ReturnProcessingSagaRepositoryImpl(ReturnProcessingSagaJpaRepository jpaRepository,
                                              ReturnProcessingSagaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ReturnProcessingSaga> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<ReturnProcessingSaga> findByReturnRequestId(String returnRequestId) {
        return jpaRepository.findByReturnRequestId(returnRequestId).map(mapper::toModel);
    }

    @Override
    public ReturnProcessingSaga save(ReturnProcessingSaga saga) {
        var entity = mapper.toEntity(saga);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
