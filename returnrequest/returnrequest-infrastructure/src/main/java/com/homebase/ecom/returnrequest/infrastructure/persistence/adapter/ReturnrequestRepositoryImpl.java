package com.homebase.ecom.returnrequest.infrastructure.persistence.adapter;

import java.util.Optional;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.domain.port.ReturnrequestRepository;
import com.homebase.ecom.returnrequest.infrastructure.persistence.entity.ReturnrequestEntity;
import com.homebase.ecom.returnrequest.infrastructure.persistence.mapper.ReturnrequestMapper;

public class ReturnrequestRepositoryImpl implements ReturnrequestRepository {

    private final ReturnrequestJpaRepository jpaRepository;
    private final ReturnrequestMapper mapper;

    public ReturnrequestRepositoryImpl(ReturnrequestJpaRepository jpaRepository, ReturnrequestMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Returnrequest> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public void save(Returnrequest returnrequest) {
        ReturnrequestEntity entity = mapper.toEntity(returnrequest);
        jpaRepository.save(entity);
        returnrequest.setId(entity.getId());
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
