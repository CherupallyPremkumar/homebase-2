package com.homebase.ecom.fulfillment.infrastructure.persistence.adapter;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import com.homebase.ecom.fulfillment.port.FulfillmentSagaRepository;
import com.homebase.ecom.fulfillment.infrastructure.persistence.entity.FulfillmentSagaEntity;
import com.homebase.ecom.fulfillment.infrastructure.persistence.mapper.FulfillmentSagaMapper;

import java.util.Optional;

public class FulfillmentSagaRepositoryImpl implements FulfillmentSagaRepository {

    private final FulfillmentSagaJpaRepository jpaRepository;
    private final FulfillmentSagaMapper mapper;

    public FulfillmentSagaRepositoryImpl(FulfillmentSagaJpaRepository jpaRepository,
                                          FulfillmentSagaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<FulfillmentSaga> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<FulfillmentSaga> findByOrderId(String orderId) {
        return jpaRepository.findByOrderId(orderId).map(mapper::toModel);
    }

    @Override
    public FulfillmentSaga save(FulfillmentSaga saga) {
        FulfillmentSagaEntity entity = mapper.toEntity(saga);
        FulfillmentSagaEntity saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public void delete(FulfillmentSaga saga) {
        FulfillmentSagaEntity entity = mapper.toEntity(saga);
        jpaRepository.delete(entity);
    }
}
