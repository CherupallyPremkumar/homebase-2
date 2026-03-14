package com.homebase.ecom.promo.client;

import com.homebase.ecom.promo.dto.PricingRequest;
import com.homebase.ecom.promo.dto.PricingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign Client for communicating with the Promo Service.
 */
@FeignClient(name = "promo-service", url = "${promo.service.url:http://promo-service}")
public interface PromoServiceClient {

    @PostMapping("/api/v1/promo/calculatePrice")
    PricingResponse calculatePrice(@RequestBody PricingRequest request);

    @GetMapping("/api/v1/promo/validateCoupon")
    boolean validateCoupon(@RequestParam("code") String code);
}
