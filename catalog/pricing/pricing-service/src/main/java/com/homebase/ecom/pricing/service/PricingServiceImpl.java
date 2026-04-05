package com.homebase.ecom.pricing.service;

import com.homebase.ecom.pricing.api.dto.*;
import com.homebase.ecom.pricing.api.service.PricingService;
import com.homebase.ecom.pricing.domain.model.LineItemPricing;
import com.homebase.ecom.pricing.domain.model.LockedPriceBreakdown;
import com.homebase.ecom.pricing.domain.model.PriceBreakdown;
import com.homebase.ecom.pricing.domain.model.PricingContext;
import com.homebase.ecom.pricing.domain.port.PriceLockPort;
import com.homebase.ecom.pricing.domain.service.IHashCalculator;
import com.homebase.ecom.pricing.domain.service.ILockTokenGenerator;

import java.util.Optional;
import org.chenile.owiz.OrchExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Pricing service implementation using OWIZ pipeline.
 * Stateless: creates PricingContext from request, runs pipeline, maps result to response.
 * lockPrice: calculates + generates lock token + stores in Redis with TTL.
 */
public class PricingServiceImpl implements PricingService {

    private static final Logger log = LoggerFactory.getLogger(PricingServiceImpl.class);
    private final OrchExecutor<PricingContext> pricingPipeline;
    private final PriceLockPort priceLockPort;
    private final ILockTokenGenerator lockTokenGenerator;
    private final IHashCalculator hashCalculator;
    private final PricingPolicyValidator policyValidator;

    public PricingServiceImpl(OrchExecutor<PricingContext> pricingPipeline,
                              PriceLockPort priceLockPort,
                              ILockTokenGenerator lockTokenGenerator,
                              IHashCalculator hashCalculator,
                              PricingPolicyValidator policyValidator) {
        this.pricingPipeline = pricingPipeline;
        this.priceLockPort = priceLockPort;
        this.lockTokenGenerator = lockTokenGenerator;
        this.hashCalculator = hashCalculator;
        this.policyValidator = policyValidator;
    }

    @Override
    public PricingResponseDTO calculatePrice(PricingRequestDTO request) {
        String validationError = validateRequest(request);
        if (validationError != null) {
            return PricingResponseDTO.error(validationError);
        }

        try {
            PricingContext ctx = buildContext(request);
            pricingPipeline.execute(ctx);
            return mapToResponse(ctx);
        } catch (Exception e) {
            log.error("Pricing calculation failed for cart: {}",
                    request.getCart().getCartId(), e);
            return PricingResponseDTO.error("Pricing calculation failed: " + e.getMessage());
        }
    }

    @Override
    public PricingResponseDTO lockPrice(PricingRequestDTO request) {
        String validationError = validateRequest(request);
        if (validationError != null) {
            return PricingResponseDTO.error(validationError);
        }

        try {
            PricingContext ctx = buildContext(request);
            pricingPipeline.execute(ctx);

            if (ctx.isError()) {
                return PricingResponseDTO.error(ctx.getErrorMessage());
            }

            PriceBreakdown breakdown = ctx.getPriceBreakdown();

            // Generate lock token and store in Redis
            String lockToken = lockTokenGenerator.generateLockToken();
            int ttlMinutes = policyValidator.getPriceLockTTLMinutes();

            LockedPriceBreakdown lock = new LockedPriceBreakdown(
                    UUID.randomUUID(), // orderId assigned later by checkout
                    breakdown,
                    lockToken,
                    ttlMinutes,
                    breakdown.getBreakdownHash());
            priceLockPort.store(lock);

            PricingResponseDTO response = mapToResponse(ctx);
            response.setLockToken(lockToken);

            log.info("Price locked for cart {}: token={}, ttl={}min, hash={}",
                    ctx.getCartId(), lockToken, ttlMinutes, breakdown.getBreakdownHash());

            return response;
        } catch (Exception e) {
            log.error("Price lock failed for cart: {}",
                    request.getCart().getCartId(), e);
            return PricingResponseDTO.error("Price lock failed: " + e.getMessage());
        }
    }

    @Override
    public PriceVerificationResponseDTO verifyPrice(PriceVerificationRequestDTO request) {
        if (request == null || request.getLockToken() == null || request.getLockToken().isBlank()) {
            return PriceVerificationResponseDTO.invalid("Lock token is required");
        }
        if (request.getBreakdownHash() == null || request.getBreakdownHash().isBlank()) {
            return PriceVerificationResponseDTO.invalid("Breakdown hash is required");
        }

        Optional<LockedPriceBreakdown> lockOpt = priceLockPort.retrieve(request.getLockToken());
        if (lockOpt.isEmpty()) {
            return PriceVerificationResponseDTO.invalid("Price lock not found or expired");
        }

        LockedPriceBreakdown lock = lockOpt.get();

        if (lock.isExpired()) {
            priceLockPort.invalidate(request.getLockToken());
            return PriceVerificationResponseDTO.invalid("Price lock has expired");
        }

        // Verify hash integrity — ensures price hasn't been tampered with
        PriceBreakdown breakdown = lock.getPriceBreakdown();
        boolean hashValid = hashCalculator.verifyHash(
                String.valueOf(breakdown.getSubtotal().getAmount()),
                String.valueOf(breakdown.getTotalDiscount().getAmount()),
                String.valueOf(breakdown.getTaxAmount().getAmount()),
                String.valueOf(breakdown.getShippingCost().getAmount()),
                String.valueOf(breakdown.getFinalTotal().getAmount()),
                request.getBreakdownHash());

        if (!hashValid) {
            log.warn("Hash mismatch for lock token {}: expected={}, got={}",
                    request.getLockToken(), breakdown.getBreakdownHash(), request.getBreakdownHash());
            return PriceVerificationResponseDTO.invalid("Price integrity check failed — hash mismatch");
        }

        // Valid — return locked price data for checkout
        PriceVerificationResponseDTO response = new PriceVerificationResponseDTO();
        response.setValid(true);
        response.setSubtotal(breakdown.getSubtotal());
        response.setTotalDiscount(breakdown.getTotalDiscount());
        response.setTaxAmount(breakdown.getTaxAmount());
        response.setShippingCost(breakdown.getShippingCost());
        response.setFinalTotal(breakdown.getFinalTotal());
        response.setBreakdownHash(breakdown.getBreakdownHash());
        response.setLockedUntil(lock.getLockedUntil());

        log.info("Price verified for lock token {}: finalTotal={}, lockedUntil={}",
                request.getLockToken(), breakdown.getFinalTotal().toDisplayString(), lock.getLockedUntil());

        return response;
    }

    private String validateRequest(PricingRequestDTO request) {
        if (request == null) return "Request must not be null";
        CartSnapshotDTO cart = request.getCart();
        if (cart == null) return "Cart snapshot must not be null";
        if (cart.getItems() == null || cart.getItems().isEmpty()) return "Cart must have at least one item";
        if (cart.getCurrency() == null || cart.getCurrency().isBlank()) return "Currency must be specified";

        for (int i = 0; i < cart.getItems().size(); i++) {
            CartItemSnapshotDTO item = cart.getItems().get(i);
            if (item.getVariantId() == null || item.getVariantId().isBlank())
                return "Item at index " + i + " must have a variantId";
            if (item.getBasePrice() == null || !item.getBasePrice().isPositive())
                return "Item " + item.getVariantId() + " must have a positive basePrice";
            if (item.getQuantity() <= 0)
                return "Item " + item.getVariantId() + " must have quantity > 0";
        }
        return null;
    }

    private PricingContext buildContext(PricingRequestDTO request) {
        CartSnapshotDTO cart = request.getCart();
        PricingContext ctx = new PricingContext(
                cart.getCartId(), cart.getUserId(), cart.getCurrency());

        // Map cart items to LineItemPricing
        List<LineItemPricing> lineItems = new ArrayList<>();
        for (CartItemSnapshotDTO item : cart.getItems()) {
            LineItemPricing lip = new LineItemPricing();
            lip.setVariantId(item.getVariantId());
            lip.setProductId(item.getProductId());
            lip.setSellerId(item.getSellerId());
            lip.setQuantity(item.getQuantity());
            lip.setUnitPrice(item.getBasePrice());
            lineItems.add(lip);
        }
        ctx.setLineItems(lineItems);

        // Coupon codes — support both single and list
        List<String> coupons = new ArrayList<>();
        if (request.getCouponCodes() != null) {
            coupons.addAll(request.getCouponCodes());
        }
        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            if (!coupons.contains(request.getCouponCode())) {
                coupons.add(request.getCouponCode());
            }
        }
        ctx.setCouponCodes(coupons);

        return ctx;
    }

    private PricingResponseDTO mapToResponse(PricingContext ctx) {
        if (ctx.isError()) {
            return PricingResponseDTO.error(ctx.getErrorMessage());
        }

        PriceBreakdown breakdown = ctx.getPriceBreakdown();
        PricingResponseDTO response = new PricingResponseDTO();
        response.setSubtotal(breakdown.getSubtotal());
        response.setTotalDiscount(breakdown.getTotalDiscount());
        response.setTaxAmount(breakdown.getTaxAmount());
        response.setShippingCost(breakdown.getShippingCost());
        response.setFinalTotal(breakdown.getFinalTotal());
        response.setBreakdownHash(breakdown.getBreakdownHash());

        // Map line items
        List<LineItemPricingDTO> lineItemDtos = new ArrayList<>();
        for (LineItemPricing item : ctx.getLineItems()) {
            LineItemPricingDTO dto = new LineItemPricingDTO();
            dto.setVariantId(item.getVariantId());
            dto.setProductId(item.getProductId());
            dto.setUnitPrice(item.getUnitPrice());
            dto.setQuantity(item.getQuantity());
            dto.setLineSubtotal(item.getLineSubtotal());
            dto.setLineDiscount(item.getLineDiscount());
            dto.setLineTotal(item.getLineTotal());
            if (!item.getAppliedDiscounts().isEmpty()) {
                dto.setDiscountReason(item.getAppliedDiscounts().stream()
                        .map(d -> d.getType() + ": " + d.getReason())
                        .reduce((a, b) -> a + "; " + b).orElse(""));
            }
            lineItemDtos.add(dto);
        }
        response.setLineItems(lineItemDtos);

        return response;
    }
}
