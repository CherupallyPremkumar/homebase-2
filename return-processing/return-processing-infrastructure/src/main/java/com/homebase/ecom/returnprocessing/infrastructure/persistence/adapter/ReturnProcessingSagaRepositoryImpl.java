package com.homebase.ecom.returnprocessing.infrastructure.persistence.adapter;

import com.homebase.ecom.returnprocessing.infrastructure.persistence.mapper.ReturnProcessingSagaMapper;
import com.homebase.ecom.returnprocessing.infrastructure.persistence.repository.ReturnProcessingSagaJpaRepository;
import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import com.homebase.ecom.returnprocessing.port.ReturnProcessingSagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ReturnProcessingSagaRepositoryImpl implements ReturnProcessingSagaRepository {

    @Autowired
    private ReturnProcessingSagaJpaRepository jpaRepository;

    @Autowired
    private ReturnProcessingSagaMapper mapper;

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
