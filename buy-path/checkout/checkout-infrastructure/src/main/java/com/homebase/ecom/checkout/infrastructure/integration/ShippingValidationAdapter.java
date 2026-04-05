package com.homebase.ecom.checkout.infrastructure.integration;

import com.homebase.ecom.checkout.domain.port.ShippingValidationPort;
import org.chenile.workflow.api.StateEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Driven adapter: validates shipping address and calculates cost via Shipping service.
 * Delegates to shipping-client's StateEntityService proxy.
 *
 * The Shipping service is STM-based and does not yet expose a dedicated
 * rate-calculation or address-validation endpoint via its client API.
 * The shipping-rate-chain (OWIZ) is internal to shipping-service.
 * When a shipping rate/validation API is added to shipping-client, this adapter
 * will delegate to it. For now, the StateEntityService dependency is injected
 * so the wiring is ready.
 */
public class ShippingValidationAdapter implements ShippingValidationPort {

    private static final Logger log = LoggerFactory.getLogger(ShippingValidationAdapter.class);

    private final StateEntityService<?> shippingServiceClient;

    public ShippingValidationAdapter(StateEntityService<?> shippingServiceClient) {
        this.shippingServiceClient = shippingServiceClient;
    }

    @Override
    public ShippingResult validate(String shippingAddressId, String shippingMethod, String currency) {
        log.info("Validating shipping for address={}, method={}, currency={}",
                shippingAddressId, shippingMethod, currency);
        // TODO: delegate to shipping-client validate endpoint
        // NOTE: ShippingService (STM) does not yet expose a rate-calculation or
        // address-validation operation via its client API. The shipping-rate-chain
        // is an internal OWIZ pipeline. When a validateAddress/calculateRate
        // endpoint is added to shipping-client, delegate here.
        // For now, return a valid result with zero cost to unblock checkout flow.
        log.warn("Shipping validation not yet available via shipping-client API. " +
                "Returning default valid result for address={}, method={}", shippingAddressId, shippingMethod);
        return new ShippingResult(true, 0L, "3-5 business days", null);
    }
}
