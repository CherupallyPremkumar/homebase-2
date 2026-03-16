package com.homebase.ecom.cart.infrastructure.adapter;

import com.homebase.ecom.cart.domain.model.PricingResult;
import com.homebase.ecom.cart.domain.port.PricingPort;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.pricing.api.dto.CartItemSnapshotDTO;
import com.homebase.ecom.pricing.api.dto.CartSnapshotDTO;
import com.homebase.ecom.pricing.api.dto.LineItemPricingDTO;
import com.homebase.ecom.pricing.api.dto.PricingRequestDTO;
import com.homebase.ecom.pricing.api.dto.PricingResponseDTO;
import com.homebase.ecom.pricing.api.service.PricingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Infrastructure adapter — TRANSLATOR ONLY.
 *
 * 1. Translates Cart → PricingRequestDTO (domain → external)
 * 2. Calls Pricing Service
 * 3. Translates PricingResponseDTO → PricingResult (external → domain)
 *
 * Does NOT mutate the Cart. Does NOT do math. Pure anti-corruption layer.
 */
public class PricingAdapter implements PricingPort {

    private static final Logger log = LoggerFactory.getLogger(PricingAdapter.class);

    private final PricingService pricingService;

    public PricingAdapter(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Override
    public PricingResult calculatePricing(Cart cart) {
        log.debug("Calling Pricing Service for cartId={}, coupons={}", cart.getId(), cart.getCouponCodes());

        PricingRequestDTO request = toRequest(cart);
        PricingResponseDTO response = pricingService.calculatePrice(request);

        if (response.isError()) {
            throw new IllegalStateException("Pricing error: " + response.getMessage());
        }

        log.debug("Pricing response for cartId={}: {}", cart.getId(), response);
        return toResult(response);
    }

    // ── Domain → External (request translation) ──

    private PricingRequestDTO toRequest(Cart cart) {
        CartSnapshotDTO snapshot = new CartSnapshotDTO();
        snapshot.setCartId(cart.getId());
        snapshot.setUserId(cart.getCustomerId());
        snapshot.setCurrency(cart.getCurrency());
        snapshot.setItems(cart.getItems().stream()
                .map(this::toItemSnapshot)
                .collect(Collectors.toList()));

        PricingRequestDTO request = new PricingRequestDTO();
        request.setCart(snapshot);
        request.setCouponCodes(cart.getCouponCodes());
        if (!cart.getCouponCodes().isEmpty()) {
            request.setCouponCode(cart.getCouponCodes().get(0));
        }
        return request;
    }

    private CartItemSnapshotDTO toItemSnapshot(com.homebase.ecom.cart.model.CartItem item) {
        CartItemSnapshotDTO dto = new CartItemSnapshotDTO();
        dto.setVariantId(item.getVariantId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        dto.setBasePrice(item.getUnitPrice());
        dto.setSellerId(item.getSupplierId());
        return dto;
    }

    // ── External → Domain (response translation) ──

    private PricingResult toResult(PricingResponseDTO response) {
        PricingResult result = new PricingResult();
        result.setSubtotal(response.getSubtotal());
        result.setTotalDiscount(response.getTotalDiscount());
        result.setTotal(response.getFinalTotal());

        if (response.getLineItems() != null) {
            result.setLineItems(response.getLineItems().stream()
                    .map(this::toLineItemPricing)
                    .collect(Collectors.toList()));
        }
        return result;
    }

    private PricingResult.LineItemPricing toLineItemPricing(LineItemPricingDTO dto) {
        PricingResult.LineItemPricing line = new PricingResult.LineItemPricing();
        line.setVariantId(dto.getVariantId());
        line.setUnitPrice(dto.getUnitPrice());
        line.setLineTotal(dto.getLineTotal());
        return line;
    }
}
