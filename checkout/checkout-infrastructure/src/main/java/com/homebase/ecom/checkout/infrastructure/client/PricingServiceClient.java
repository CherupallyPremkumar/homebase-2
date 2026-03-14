package com.homebase.ecom.checkout.infrastructure.client;

import com.homebase.ecom.checkout.domain.model.CartSnapshot;
import com.homebase.ecom.checkout.domain.model.PriceSnapshot;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@FeignClient(name = "pricing-service", url = "${pricing.service.url:http://localhost:8081}")
public interface PricingServiceClient {
    @PostMapping("/api/pricing/lock")
    PriceSnapshot lockPrice(@RequestBody CartSnapshot cart, @RequestParam UUID userId, @RequestParam(required = false) String couponCode);

    @DeleteMapping("/api/pricing/locks/{lockToken}")
    void releaseLock(@PathVariable String lockToken);
}
