package com.homebase.ecom.cart.client.delegate;

import com.homebase.ecom.cart.dto.*;
import com.homebase.ecom.cart.service.CartService;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Business delegate implementation for Cart service.
 * Wraps the Chenile Proxy and translates typed method calls
 * into CartService.processById(id, eventId, payload).
 *
 * Follows the Chenile process-delegate pattern.
 */
public class CartManagerClientImpl implements CartManagerClient {

    @Autowired
    @Qualifier("cartServiceProxy")
    private CartService cartServiceProxy;

    // ── Create & Retrieve ────────────────────────────────────────────

    @Override
    public CartDto create(CreateCartPayload payload) {
        return cartServiceProxy.createCart(payload);
    }

    @Override
    public CartDto getCart(String cartId) {
        return cartServiceProxy.getCart(cartId);
    }

    // ── Item management ──────────────────────────────────────────────

    @Override
    public CartDto addItem(String cartId, AddItemCartPayload payload) {
        return cartServiceProxy.proceedById(cartId, "addItem", payload);
    }

    @Override
    public CartDto removeItem(String cartId, String variantId) {
        RemoveItemCartPayload payload = new RemoveItemCartPayload();
        payload.variantId = variantId;
        return cartServiceProxy.proceedById(cartId, "removeItem", payload);
    }

    @Override
    public CartDto updateQuantity(String cartId, String variantId, int quantity) {
        UpdateQuantityCartPayload payload = new UpdateQuantityCartPayload();
        payload.variantId = variantId;
        payload.quantity = quantity;
        return cartServiceProxy.proceedById(cartId, "updateQuantity", payload);
    }

    // ── Coupon management ────────────────────────────────────────────

    @Override
    public CartDto applyCoupon(String cartId, String couponCode) {
        ApplyCouponCartPayload payload = new ApplyCouponCartPayload();
        payload.couponCode = couponCode;
        return cartServiceProxy.proceedById(cartId, "applyCoupon", payload);
    }

    @Override
    public CartDto removeCoupon(String cartId, String couponCode) {
        RemoveCouponCartPayload payload = new RemoveCouponCartPayload();
        payload.couponCode = couponCode;
        return cartServiceProxy.proceedById(cartId, "removeCoupon", payload);
    }

    // ── Cart merging ─────────────────────────────────────────────────

    @Override
    public CartDto merge(String targetCartId, String sourceCartId) {
        MergeCartPayload payload = new MergeCartPayload();
        payload.sourceCartId = sourceCartId;
        payload.setComment("Merging cart " + sourceCartId);
        return cartServiceProxy.proceedById(targetCartId, "merge", payload);
    }

    // ── Checkout lifecycle ───────────────────────────────────────────

    @Override
    public CartDto initiateCheckout(String cartId) {
        MinimalPayload payload = new MinimalPayload();
        payload.setComment("Initiating checkout");
        return cartServiceProxy.proceedById(cartId, "initiateCheckout", payload);
    }

    @Override
    public CartDto cancelCheckout(String cartId) {
        MinimalPayload payload = new MinimalPayload();
        payload.setComment("Cancelling checkout");
        return cartServiceProxy.proceedById(cartId, "cancelCheckout", payload);
    }

    @Override
    public CartDto completeCheckout(String cartId, String orderId) {
        CompleteCheckoutCartPayload payload = new CompleteCheckoutCartPayload();
        payload.orderId = orderId;
        return cartServiceProxy.proceedById(cartId, "completeCheckout", payload);
    }

    // ── Cart lifecycle ───────────────────────────────────────────────

    @Override
    public CartDto abandon(String cartId) {
        MinimalPayload payload = new MinimalPayload();
        payload.setComment("Cart abandoned");
        return cartServiceProxy.proceedById(cartId, "abandon", payload);
    }

    @Override
    public CartDto expire(String cartId) {
        MinimalPayload payload = new MinimalPayload();
        payload.setComment("Cart expired");
        return cartServiceProxy.proceedById(cartId, "expire", payload);
    }

    @Override
    public CartDto reactivate(String cartId) {
        MinimalPayload payload = new MinimalPayload();
        payload.setComment("Cart reactivated");
        return cartServiceProxy.proceedById(cartId, "reactivate", payload);
    }

    // ── Generic event ────────────────────────────────────────────────

    @Override
    public CartDto process(String cartId, String eventId, Object payload) {
        return cartServiceProxy.proceedById(cartId, eventId, payload);
    }
}
