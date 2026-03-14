package com.homebase.ecom.fulfillment.infrastructure.persistence;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import com.homebase.ecom.fulfillment.infrastructure.persistence.adapter.FulfillmentSagaJpaRepository;
import com.homebase.ecom.fulfillment.infrastructure.persistence.entity.FulfillmentSagaEntity;
import com.homebase.ecom.fulfillment.infrastructure.persistence.mapper.FulfillmentSagaMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

public class ChenileFulfillmentSagaEntityStore extends ChenileJpaEntityStore<FulfillmentSaga, FulfillmentSagaEntity> {

    public ChenileFulfillmentSagaEntityStore(FulfillmentSagaJpaRepository repository,
                                              FulfillmentSagaMapper mapper) {
        super(repository, (entity) -> mapper.toModel(entity), (model) -> mapper.toEntity(model));
    }
}
