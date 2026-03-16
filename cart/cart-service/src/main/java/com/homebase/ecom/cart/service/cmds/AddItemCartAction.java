package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.AddItemCartPayload;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.shared.Money;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import java.time.LocalDateTime;

/**
 * STM transition action for addItem event.
 * Validates item count and quantity via CartPolicyValidator, then adds item to cart.
 */
public class AddItemCartAction extends AbstractCartAction<AddItemCartPayload> {

    @Override
    public void transitionTo(Cart cart, AddItemCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // variantId is required — it's the cart line item key
        if (payload.variantId == null || payload.variantId.isBlank()) {
            throw new IllegalArgumentException("variantId is required");
        }

        // Verify variant exists under product and product is sellable
        if (!productCheckPort.variantExists(payload.productId, payload.variantId)) {
            throw new IllegalArgumentException(
                    "Variant " + payload.variantId + " not found under product " + payload.productId
                    + ", or product is not active");
        }

        // Enforce single currency — item currency must match cart currency
        if (payload.currency != null && cart.getCurrency() != null
                && !cart.getCurrency().equals(payload.currency)) {
            throw new IllegalArgumentException(
                    "Currency mismatch: cart uses " + cart.getCurrency() + " but item uses " + payload.currency);
        }

        // Check if this is a new line item (by variantId) — validate item count
        boolean isNewItem = cart.getItems().stream()
                .noneMatch(i -> i.getVariantId().equals(payload.variantId));

        if (isNewItem) {
            cartPolicyValidator.validateItemCount(cart);
        }

        // Determine final quantity for this variant
        int existingQty = cart.getItems().stream()
                .filter(i -> i.getVariantId().equals(payload.variantId))
                .mapToInt(CartItem::getQuantity)
                .findFirst().orElse(0);
        int totalQty = existingQty + payload.quantity;
        cartPolicyValidator.validateQuantity(totalQty);

        // Check inventory availability by variantId
        if (!inventoryCheckPort.isAvailable(payload.variantId, totalQty)) {
            throw new IllegalStateException("Insufficient inventory for variant: " + payload.variantId);
        }

        // Build cart item and add (duplicate variantId merges quantity automatically)
        CartItem newItem = new CartItem();
        newItem.setProductId(payload.productId);
        newItem.setVariantId(payload.variantId);
        newItem.setSku(payload.sku);
        newItem.setProductName(payload.productName);
        newItem.setQuantity(payload.quantity);
        newItem.setUnitPrice(Money.of(payload.unitPrice, cart.getCurrency()));

        cart.addItem(newItem);

        // Set expiration if not already set
        if (cart.getExpiresAt() == null) {
            int expirationHours = cartPolicyValidator.getCartExpirationHours();
            cart.setExpiresAt(LocalDateTime.now().plusHours(expirationHours));
        }

        logActivity(cart, "addItem", "Added variant " + payload.variantId + " qty " + payload.quantity);
    }
}
