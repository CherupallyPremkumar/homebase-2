package com.homebase.ecom.pricing.service.command;

import com.homebase.ecom.pricing.domain.model.AppliedPromotion;
import com.homebase.ecom.pricing.domain.model.LineItemPricing;
import com.homebase.ecom.pricing.domain.model.PricingContext;
import com.homebase.ecom.pricing.domain.port.PromoValidationPort;
import com.homebase.ecom.pricing.domain.port.PromoValidationPort.CouponResult;
import com.homebase.ecom.pricing.service.PricingPolicyValidator;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Step 5: Validate and apply coupon codes (cart-level discount).
 * Calls PromoValidationPort to validate each coupon, then applies discount.
 * Respects maxCouponsPerCart from cconfig.
 */
public class ApplyCouponCommand implements Command<PricingContext> {

    private static final Logger log = LoggerFactory.getLogger(ApplyCouponCommand.class);
    private final PromoValidationPort promoValidationPort;
    private final PricingPolicyValidator policyValidator;

    public ApplyCouponCommand(PromoValidationPort promoValidationPort, PricingPolicyValidator policyValidator) {
        this.promoValidationPort = promoValidationPort;
        this.policyValidator = policyValidator;
    }

    @Override
    public void execute(PricingContext ctx) throws Exception {
        if (ctx.getCouponCodes() == null || ctx.getCouponCodes().isEmpty()) return;

        int maxCoupons = policyValidator.getMaxCouponsPerCart();
        Money runningSubtotal = calculateCurrentSubtotal(ctx);
        int appliedCount = 0;

        for (String code : ctx.getCouponCodes()) {
            if (appliedCount >= maxCoupons) {
                log.warn("Max coupons ({}) reached, skipping: {}", maxCoupons, code);
                break;
            }

            CouponResult result = promoValidationPort.validate(code, runningSubtotal, ctx.getUserId());
            if (!result.isValid()) {
                log.info("Coupon {} invalid: {}", code, result.getErrorMessage());
                continue;
            }

            Money couponDiscount = result.getDiscountAmount();
            if (couponDiscount != null && couponDiscount.isPositive()) {
                ctx.setTotalDiscount(ctx.getTotalDiscount().add(couponDiscount));

                ctx.getAppliedPromotions().add(AppliedPromotion.builder()
                        .promotionName(result.getPromotionName())
                        .discountAmount(couponDiscount)
                        .discountPercent(BigDecimal.valueOf(result.getDiscountPercent()))
                        .strategy(result.getStrategy())
                        .appliedAt(LocalDateTime.now())
                        .build());

                appliedCount++;
                log.debug("Coupon {} applied: -{}", code, couponDiscount.toDisplayString());
            }
        }
    }

    private Money calculateCurrentSubtotal(PricingContext ctx) {
        Money total = Money.zero(ctx.getCurrency());
        for (LineItemPricing item : ctx.getLineItems()) {
            total = total.add(item.getCurrentPrice().multiply(item.getQuantity()));
        }
        return total;
    }
}
