package com.homebase.ecom.returnprocessing.service;

import org.chenile.base.response.GenericResponse;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;

/**
 * Service interface for return processing saga operations, intended to be used via Chenile Proxy.
 * The saga orchestrates across: returnrequest, shipping, inventory, settlement, payment BCs.
 */
public interface ReturnProcessingService {

    /**
     * Triggers a state transition for a return processing saga by its ID.
     *
     * @param id      The ID of the saga.
     * @param eventId The ID of the event to trigger.
     * @param payload The payload for the transition (can be null).
     * @return The response containing the updated saga state.
     */
    GenericResponse<StateEntityServiceResponse<ReturnProcessingSaga>> proceedById(
            String id, String eventId, Object payload);

    /**
     * Retrieves a return processing saga by its ID.
     */
    ReturnProcessingSaga getSaga(String id);
}
