package com.homebase.ecom.fulfillment.port;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * Outbound port for publishing fulfillment domain events.
 * Infrastructure layer provides Kafka implementation.
 * Domain/service layer depends only on this interface -- never on ChenilePub directly.
 */
public interface FulfillmentEventPublisherPort {

    void publishInventoryReserved(FulfillmentSaga saga);

    void publishShipmentCreated(FulfillmentSaga saga);

    void publishShipped(FulfillmentSaga saga);

    void publishCustomerNotified(FulfillmentSaga saga);

    void publishCompleted(FulfillmentSaga saga);

    void publishFailed(FulfillmentSaga saga, String previousState);

    void publishCompensationDone(FulfillmentSaga saga);
}
