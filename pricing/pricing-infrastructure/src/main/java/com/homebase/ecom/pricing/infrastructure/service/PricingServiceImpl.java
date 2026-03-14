package com.homebase.ecom.pricing.infrastructure.service;

import com.homebase.ecom.shared.Money;
import com.homebase.ecom.pricing.domain.model.PriceBreakdown;
import com.homebase.ecom.pricing.domain.model.LockedPriceBreakdown;
import com.homebase.ecom.pricing.domain.service.IPriceCalculator;
import com.homebase.ecom.pricing.infrastructure.security.HashCalculator;
import com.homebase.ecom.pricing.infrastructure.security.LockTokenGenerator;
import com.homebase.ecom.pricing.infrastructure.client.PromoServiceClient;
import com.homebase.ecom.pricing.infrastructure.client.dto.ValidateCouponRequest;
import com.homebase.ecom.pricing.infrastructure.client.dto.CouponValidationResult;
import com.homebase.ecom.pricing.infrastructure.cache.PriceLockCacheManager;
import com.homebase.ecom.pricing.infrastructure.event.PriceLockedEvent;
import com.homebase.ecom.pricing.infrastructure.event.PricingEventPublisher;
import com.homebase.ecom.pricing.domain.model.AppliedPromotion;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PricingServiceImpl implements IPriceCalculator {

    private static final Logger log = LoggerFactory.getLogger(PricingServiceImpl.class);

    private final HashCalculator hashCalculator;
    private final LockTokenGenerator lockTokenGenerator;
    private final PriceLockCacheManager cacheManager;
    private final PromoServiceClient promoClient;
    private final PricingEventPublisher eventPublisher;

    public PricingServiceImpl(HashCalculator hashCalculator, LockTokenGenerator lockTokenGenerator,
                              PriceLockCacheManager cacheManager, PromoServiceClient promoClient,
                              PricingEventPublisher eventPublisher) {
        this.hashCalculator = hashCalculator;
        this.lockTokenGenerator = lockTokenGenerator;
        this.cacheManager = cacheManager;
        this.promoClient = promoClient;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @CircuitBreaker(name = "promo-service", fallbackMethod = "fallbackCalculatePrice")
    public PriceBreakdown calculatePrice(Object cartSnapshot, String couponCode) {
        // In a real scenario, extract details from cartSnapshot
        Money subtotal = new Money(new BigDecimal("1000.00"), "INR");
        Money discount = new Money(BigDecimal.ZERO, "INR");
        List<AppliedPromotion> appliedPromos = new ArrayList<>();

        if (couponCode != null && !couponCode.isEmpty()) {
            ValidateCouponRequest request = ValidateCouponRequest.builder()
                .code(couponCode)
                .cartTotal(subtotal.getAmount())
                .build();

            CouponValidationResult result = promoClient.validateCoupon(request);

            if (result.isValid()) {
                discount = new Money(result.getSavingsAmount(), "INR");
                appliedPromos.add(AppliedPromotion.builder()
                    .promotionId(UUID.fromString(result.getPromotionId()))
                    .promotionName(result.getPromotionName())
                    .discountAmount(discount)
                    .strategy(result.getStrategy())
                    .appliedAt(LocalDateTime.now())
                    .build());
            }
        }

        Money tax = new Money(new BigDecimal("50.00"), "INR");
        Money shipping = new Money(new BigDecimal("40.00"), "INR");
        Money finalTotal = subtotal.subtract(discount).add(tax).add(shipping);

        String hash = hashCalculator.calculateBreakdownHash(
                subtotal.getAmount().toString(),
                discount.getAmount().toString(),
                tax.getAmount().toString(),
                shipping.getAmount().toString(),
                finalTotal.getAmount().toString()
        );

        return PriceBreakdown.builder()
                .subtotal(subtotal)
                .totalDiscount(discount)
                .taxAmount(tax)
                .shippingCost(shipping)
                .finalTotal(finalTotal)
                .breakdownHash(hash)
                .currency("INR")
                .itemDiscounts(new ArrayList<>())
                .appliedPromos(appliedPromos)
                .calculatedAt(LocalDateTime.now())
                .build();
    }

    public PriceBreakdown fallbackCalculatePrice(Object cartSnapshot, String couponCode, Exception e) {
        log.warn("Promo service unavailable, calculating without discount for coupon: {}. Error: {}", couponCode, e.getMessage());
        return calculatePrice(cartSnapshot, null);
    }

    @Override
    public LockedPriceBreakdown lockPrice(UUID orderId, Object cartSnapshot, String couponCode) {
        PriceBreakdown breakdown = calculatePrice(cartSnapshot, couponCode);
        String token = lockTokenGenerator.generateLockToken();

        LockedPriceBreakdown locked = new LockedPriceBreakdown(
                orderId, breakdown, token, 15, breakdown.getBreakdownHash()
        );

        cacheManager.storeLock(locked);

        eventPublisher.publishPriceLocked(PriceLockedEvent.builder()
            .eventId(UUID.randomUUID().toString())
            .orderId(orderId.toString())
            .cartId("placeholder-cart-id") // Should come from snapshot
            .occurredAt(LocalDateTime.now())
            .finalTotal(breakdown.getFinalTotal())
            .totalDiscount(breakdown.getTotalDiscount())
            .breakdownHash(breakdown.getBreakdownHash())
            .lockToken(token)
            .lockedUntil(LocalDateTime.now().plusMinutes(15))
            .lockDurationMinutes(15)
            .appliedCouponCode(couponCode)
            .build());

        return locked;
    }
}
