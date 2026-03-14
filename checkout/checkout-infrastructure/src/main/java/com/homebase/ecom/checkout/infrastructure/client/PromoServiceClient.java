package com.homebase.ecom.checkout.infrastructure.client;

import com.homebase.ecom.shared.Money;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@FeignClient(name = "promo-service", url = "${app.services.promo-url}")
public interface PromoServiceClient {

    @PostMapping("/api/promos/validate")
    CouponValidationResponse validateCoupon(@RequestBody CouponValidationRequest request);

    @PostMapping("/api/promos/commit")
    void commitPromo(@RequestBody String couponCode, @RequestBody UUID userId);

    @PostMapping("/api/promos/release")
    void releasePromo(@RequestBody String couponCode, @RequestBody UUID userId);

    class CouponValidationRequest {
        private String couponCode;
        private UUID userId;
        private UUID cartId;
        public String getCouponCode() { return couponCode; }
        public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
        public UUID getUserId() { return userId; }
        public void setUserId(UUID userId) { this.userId = userId; }
        public UUID getCartId() { return cartId; }
        public void setCartId(UUID cartId) { this.cartId = cartId; }
    }

    class CouponValidationResponse {
        private boolean valid;
        private String message;
        private Money discountAmount;
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Money getDiscountAmount() { return discountAmount; }
        public void setDiscountAmount(Money discountAmount) { this.discountAmount = discountAmount; }
    }
}
