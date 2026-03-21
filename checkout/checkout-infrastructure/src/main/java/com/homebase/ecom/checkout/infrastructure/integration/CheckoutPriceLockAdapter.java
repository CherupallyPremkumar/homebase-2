package com.homebase.ecom.checkout.infrastructure.integration;

import com.homebase.ecom.checkout.domain.port.PriceLockPort;
import com.homebase.ecom.checkout.model.CheckoutItem;
import com.homebase.ecom.pricing.api.dto.CartItemSnapshotDTO;
import com.homebase.ecom.pricing.api.dto.CartSnapshotDTO;
import com.homebase.ecom.pricing.api.dto.PricingRequestDTO;
import com.homebase.ecom.pricing.api.dto.PricingResponseDTO;
import com.homebase.ecom.pricing.api.service.PricingService;
import com.homebase.ecom.shared.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Driven adapter: locks prices via Pricing service.
 * Delegates to pricing-client's PricingService.lockPrice() which calculates prices
 * and stores them in Redis with a TTL for checkout integrity.
 *
 * Translates CheckoutItem list to PricingRequestDTO (domain -> external),
 * then PricingResponseDTO back to LockedPrice (external -> domain).
 */
public class CheckoutPriceLockAdapter implements PriceLockPort {

    private static final Logger log = LoggerFactory.getLogger(CheckoutPriceLockAdapter.class);

    private final PricingService pricingService;

    public CheckoutPriceLockAdapter(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Override
    public LockedPrice lockPrice(String cartId, String customerId, String currency,
                                  List<CheckoutItem> items, List<String> couponCodes) {
        log.info("Locking prices for cart={}, customer={}, currency={}, itemCount={}, coupons={}",
                cartId, customerId, currency, items.size(), couponCodes);
        // TODO: delegate to pricing-client lockPrice endpoint
        PricingRequestDTO request = toRequest(cartId, customerId, currency, items, couponCodes);
        PricingResponseDTO response = pricingService.lockPrice(request);

        if (response.isError()) {
            log.error("Price lock failed for cart={}: {}", cartId, response.getMessage());
            throw new RuntimeException("Price lock failed: " + response.getMessage());
        }

        LockedPrice lockedPrice = toLockedPrice(response, currency);
        log.info("Prices locked for cart={}, lockToken={}, finalTotal={}",
                cartId, lockedPrice.lockToken(), lockedPrice.finalTotal());
        return lockedPrice;
    }

    // -- Domain -> External (request translation) --

    private PricingRequestDTO toRequest(String cartId, String customerId, String currency,
                                        List<CheckoutItem> items, List<String> couponCodes) {
        CartSnapshotDTO snapshot = new CartSnapshotDTO();
        snapshot.setCartId(cartId);
        snapshot.setUserId(customerId);
        snapshot.setCurrency(currency);
        snapshot.setItems(items.stream()
                .map(this::toItemSnapshot)
                .collect(Collectors.toList()));

        PricingRequestDTO request = new PricingRequestDTO();
        request.setCart(snapshot);
        request.setCouponCodes(couponCodes);
        if (couponCodes != null && !couponCodes.isEmpty()) {
            request.setCouponCode(couponCodes.get(0));
        }
        return request;
    }

    private CartItemSnapshotDTO toItemSnapshot(CheckoutItem item) {
        CartItemSnapshotDTO dto = new CartItemSnapshotDTO();
        dto.setVariantId(item.getVariantId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        dto.setBasePrice(item.getUnitPrice());
        dto.setSellerId(item.getSupplierId());
        return dto;
    }

    // -- External -> Domain (response translation) --

    private LockedPrice toLockedPrice(PricingResponseDTO response, String currency) {
        return new LockedPrice(
                moneyToAmount(response.getSubtotal()),
                moneyToAmount(response.getTotalDiscount()),
                moneyToAmount(response.getTaxAmount()),
                moneyToAmount(response.getShippingCost()),
                moneyToAmount(response.getFinalTotal()),
                response.getLockToken() != null ? response.getLockToken() : "",
                response.getBreakdownHash() != null ? response.getBreakdownHash() : "",
                currency
        );
    }

    private long moneyToAmount(Money money) {
        return money != null ? money.getAmount() : 0L;
    }
}
