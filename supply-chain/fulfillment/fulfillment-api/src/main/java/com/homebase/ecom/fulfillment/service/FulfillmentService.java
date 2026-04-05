package com.homebase.ecom.fulfillment.service;

import com.homebase.ecom.fulfillment.dto.FulfillmentRequest;
import com.homebase.ecom.fulfillment.dto.FulfillmentResponse;

/**
 * Service interface for the fulfillment orchestration flow.
 * Coordinates: Order PAID -> Deduct Inventory -> Create Shipment -> Notify Customer.
 */
public interface FulfillmentService {

    /**
     * Executes the fulfillment flow for the given request.
     *
     * @param request the fulfillment request containing orderId and userId
     * @return the fulfillment response with shipment and tracking details
     */
    FulfillmentResponse fulfill(FulfillmentRequest request);
}
