package com.homebase.ecom.fulfillment.port;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

import java.util.Optional;

/**
 * Port interface for FulfillmentSaga persistence.
 * Implemented by the infrastructure layer.
 */
public interface FulfillmentSagaRepository {

    Optional<FulfillmentSaga> findById(String id);

    Optional<FulfillmentSaga> findByOrderId(String orderId);

    FulfillmentSaga save(FulfillmentSaga saga);

    void delete(FulfillmentSaga saga);
}
