package com.homebase.ecom.pricing.domain.port;

import com.homebase.ecom.shared.Money;
import java.util.Optional;

/**
 * Port for validating and applying coupon codes.
 * Infrastructure adapter calls Promo service via ProxyBuilder.
 */
public interface PromoValidationPort {

    CouponResult validate(String couponCode, Money cartTotal, String userId);

    class CouponResult {
        private boolean valid;
        private String couponCode;
        private String promotionName;
        private Money discountAmount;
        private int discountPercent;
        private String strategy; // FLAT or PERCENTAGE
        private String errorMessage;

        public CouponResult() {}

        public static CouponResult invalid(String code, String reason) {
            CouponResult r = new CouponResult();
            r.valid = false;
            r.couponCode = code;
            r.errorMessage = reason;
            return r;
        }

        public static CouponResult valid(String code, String name, Money amount, int percent, String strategy) {
            CouponResult r = new CouponResult();
            r.valid = true;
            r.couponCode = code;
            r.promotionName = name;
            r.discountAmount = amount;
            r.discountPercent = percent;
            r.strategy = strategy;
            return r;
        }

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getCouponCode() { return couponCode; }
        public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
        public String getPromotionName() { return promotionName; }
        public void setPromotionName(String promotionName) { this.promotionName = promotionName; }
        public Money getDiscountAmount() { return discountAmount; }
        public void setDiscountAmount(Money discountAmount) { this.discountAmount = discountAmount; }
        public int getDiscountPercent() { return discountPercent; }
        public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }
        public String getStrategy() { return strategy; }
        public void setStrategy(String strategy) { this.strategy = strategy; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }
}
