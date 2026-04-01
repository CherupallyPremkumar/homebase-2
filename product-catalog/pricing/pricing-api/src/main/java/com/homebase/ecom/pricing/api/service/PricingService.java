package com.homebase.ecom.pricing.api.service;

import com.homebase.ecom.pricing.api.dto.PriceVerificationRequestDTO;
import com.homebase.ecom.pricing.api.dto.PriceVerificationResponseDTO;
import com.homebase.ecom.pricing.api.dto.PricingRequestDTO;
import com.homebase.ecom.pricing.api.dto.PricingResponseDTO;

/**
 * Service interface for the Pricing engine.
 * Used by ProxyBuilder to create a client proxy.
 *
 * 3 phases:
 *   1. calculatePrice  — stateless calculation (cart browsing)
 *   2. lockPrice        — calculate + store in Redis with TTL (checkout initiation)
 *   3. verifyPrice      — confirm lock is valid + hash matches (before payment)
 */
public interface PricingService {
    PricingResponseDTO calculatePrice(PricingRequestDTO request);
    PricingResponseDTO lockPrice(PricingRequestDTO request);
    PriceVerificationResponseDTO verifyPrice(PriceVerificationRequestDTO request);
}
