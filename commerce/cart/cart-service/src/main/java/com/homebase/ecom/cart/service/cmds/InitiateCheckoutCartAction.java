package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

import java.time.LocalDateTime;

/**
 * STM transition action for initiateCheckout event.
 * Validates cart, recalculates pricing, verifies all items are still available,
 * then transitions to CHECKOUT_INITIATED.
 *
 * Note: actual inventory reservation is Checkout saga's responsibility (separate BC).
 * Cart just validates availability before handing off.
 */
public class InitiateCheckoutCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart");
        }

        // Guest checkout check
        if (!cartPolicyValidator.isGuestCheckoutAllowed()
                && (cart.getCustomerId() == null || cart.getCustomerId().isBlank())) {
            throw new IllegalStateException("Guest checkout is not allowed. Please login to proceed.");
        }

        // Final pricing recalculation + max cart value check
        recalculatePricingAndValidateValue(cart);

        // Validate minimum checkout amount (against total — accounts for discounts)
        long minAmount = cartPolicyValidator.getMinCheckoutAmount();
        if (cart.getTotal() != null && cart.getTotal().getAmount() < minAmount) {
            throw new IllegalStateException(
                    "Cart total " + cart.getTotal().toDisplayString()
                    + " is below minimum checkout amount of " + minAmount + " " + cart.getCurrency());
        }

        // Verify all items are still available before handing off to checkout
        for (CartItem item : cart.getItems()) {
            if (!inventoryCheckPort.isAvailable(item.getVariantId(), item.getQuantity())) {
                throw new IllegalStateException(
                        "Insufficient inventory for " + item.getProductName()
                        + " (variant: " + item.getVariantId() + ")");
            }
        }

        // Set checkout reservation expiry (Checkout saga should complete within this window)
        int reservationMinutes = cartPolicyValidator.getCheckoutReservationMinutes();
        cart.setExpiresAt(LocalDateTime.now().plusMinutes(reservationMinutes));

        logActivity(cart, "initiateCheckout",
                "Cart locked for checkout, items: " + cart.getItems().size()
                + ", total: " + cart.getTotal()
                + ", reservation expires in " + reservationMinutes + " min");
    }
}
