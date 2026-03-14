package com.homebase.ecom.pricing.infrastructure.client;

import com.homebase.ecom.pricing.infrastructure.client.dto.ValidateCouponRequest;
import com.homebase.ecom.pricing.infrastructure.client.dto.CouponValidationResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "promo-service", url = "${promo.service.url:http://localhost:8082}")
public interface PromoServiceClient {

    @PostMapping("/promos/validate-coupon")
    CouponValidationResult validateCoupon(@RequestBody ValidateCouponRequest request);
}
