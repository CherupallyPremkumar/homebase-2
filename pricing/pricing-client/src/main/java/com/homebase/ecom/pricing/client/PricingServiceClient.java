package com.homebase.ecom.pricing.client;

import com.homebase.ecom.pricing.api.dto.PricingRequestDTO;
import com.homebase.ecom.pricing.api.dto.PricingResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign Client for communicating with the Pricing Service.
 */
@FeignClient(name = "pricing-service", url = "${pricing.service.url:http://pricing-service}")
public interface PricingServiceClient {

    @PostMapping("/api/v1/pricing/calculate")
    PricingResponseDTO calculatePrices(@RequestBody PricingRequestDTO request);
}
