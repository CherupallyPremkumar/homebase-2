package com.homebase.ecom.cart.service;

import org.chenile.base.response.GenericResponse;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.homebase.ecom.cart.dto.CartDto;

/**
 * Service interface for Cart operations, intended to be used via Chenile Proxy.
 */
public interface CartService {

    /**
     * Triggers a state transition for a cart by its ID.
     * 
     * @param id      The ID of the cart.
     * @param eventId The ID of the event to trigger.
     * @param payload The payload for the transition (can be null).
     * @return The response containing the updated cart state.
     */
    GenericResponse<StateEntityServiceResponse<CartDto>> proceedById(String id, String eventId, Object payload);

    /**
     * Retrieves a cart by its ID.
     */
    CartDto getCart(String id);
}
