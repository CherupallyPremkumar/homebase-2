package com.homebase.ecom.cart.client.delegate;

import com.homebase.ecom.cart.dto.AddItemCartPayload;
import com.homebase.ecom.cart.dto.CartDto;
import com.homebase.ecom.cart.dto.CreateCartPayload;

/**
 * Business delegate interface for Cart service.
 * Provides typed, domain-specific methods instead of raw proceedById.
 * Follows the Chenile process-delegate pattern.
 */
public interface CartManagerClient {

    // ── Create & Retrieve ────────────────────────────────────────────

    CartDto create(CreateCartPayload payload);

    CartDto getCart(String cartId);

    // ── Item management ──────────────────────────────────────────────

    CartDto addItem(String cartId, AddItemCartPayload payload);

    CartDto removeItem(String cartId, String variantId);

    CartDto updateQuantity(String cartId, String variantId, int quantity);

    // ── Coupon management ────────────────────────────────────────────

    CartDto applyCoupon(String cartId, String couponCode);

    CartDto removeCoupon(String cartId, String couponCode);

    // ── Cart merging ─────────────────────────────────────────────────

    CartDto merge(String targetCartId, String sourceCartId);

    // ── Checkout lifecycle ───────────────────────────────────────────

    CartDto initiateCheckout(String cartId);

    CartDto cancelCheckout(String cartId);

    CartDto completeCheckout(String cartId, String orderId);

    // ── Cart lifecycle ───────────────────────────────────────────────

    CartDto abandon(String cartId);

    CartDto expire(String cartId);

    CartDto reactivate(String cartId);

    // ── Generic event (escape hatch) ─────────────────────────────────

    CartDto process(String cartId, String eventId, Object payload);
}
