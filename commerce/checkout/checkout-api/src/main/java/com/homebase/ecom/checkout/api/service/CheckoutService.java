package com.homebase.ecom.checkout.api.service;

import org.chenile.base.response.GenericResponse;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.homebase.ecom.checkout.dto.CheckoutDto;

/**
 * Service interface for Checkout operations, intended to be used via Chenile Proxy.
 */
public interface CheckoutService {

    GenericResponse<StateEntityServiceResponse<CheckoutDto>> proceedById(String id, String eventId, Object payload);

    CheckoutDto getCheckout(String id);
}
