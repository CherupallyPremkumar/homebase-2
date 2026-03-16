package com.homebase.ecom.pricing.api.service;

import com.homebase.ecom.pricing.api.dto.PricingRequestDTO;
import com.homebase.ecom.pricing.api.dto.PricingResponseDTO;

/**
 * Service interface for the Pricing engine.
 * Used by ProxyBuilder to create a client proxy.
 */
public interface PricingService {
    PricingResponseDTO calculatePrice(PricingRequestDTO request);
    PricingResponseDTO lockPrice(PricingRequestDTO request);
}
