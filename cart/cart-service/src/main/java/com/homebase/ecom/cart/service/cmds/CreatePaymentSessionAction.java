package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.cart.model.CartItemStatus;
import com.homebase.ecom.shared.Money;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

/**
 * Action to create a payment session for the cart.
 * Builds a payment initiation request from the cart, generates an idempotency key,
 * and stores the session details for webhook correlation.
 */
public class CreatePaymentSessionAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        validateCustomerAccess();

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot create payment session for empty cart");
        }

        // Verify cart has a total amount
        if (cart.getTotalAmount() == null || cart.getTotalAmount().isZero()) {
            throw new RuntimeException("Cart total amount must be calculated before creating payment session");
        }

        // Check for unavailable items
        boolean hasUnavailable = cart.getItems().stream()
                .anyMatch(item -> item.getStatus() != CartItemStatus.AVAILABLE);
        if (hasUnavailable) {
            throw new RuntimeException("Cannot proceed to payment with unavailable items in cart");
        }

        // Build payment details from cart
        BigDecimal totalAmount = cart.getTotalAmount().getAmount();
        String currency = cart.getTotalAmount().getCurrency();
        int itemCount = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        // Generate idempotency key from cartId + timestamp hash
        String idempotencyKey = generateIdempotencyKey(cart.getId());

        // Store session details in transient map for the payment gateway
        cart.getTransientMap().put("paymentAmount", totalAmount.toPlainString());
        cart.getTransientMap().put("paymentCurrency", currency);
        cart.getTransientMap().put("paymentItemCount", String.valueOf(itemCount));
        cart.getTransientMap().put("idempotencyKey", idempotencyKey);

        // Generate a session ID for webhook correlation
        String gatewaySessionId = "sess_" + cart.getId() + "_" + System.currentTimeMillis();
        cart.getTransientMap().put("gatewaySessionId", gatewaySessionId);

        // The checkout URL will be populated by the edge/gateway integration layer
        // Here we set a placeholder that the orchestration layer will replace
        String checkoutUrl = "/checkout/pay?session=" + gatewaySessionId;
        cart.getTransientMap().put("checkoutUrl", checkoutUrl);

        logActivity(cart, "Create Payment Session",
                String.format("Payment session created. Amount: %s %s, Items: %d, Session: %s",
                        totalAmount.toPlainString(), currency, itemCount, gatewaySessionId));
        cart.addActivity(eventId, "Payment session created for " + totalAmount.toPlainString() + " " + currency);
    }

    /**
     * Generates a deterministic idempotency key from cartId and current timestamp bucket.
     * Uses a coarse timestamp (minute-level) to allow retries within the same minute.
     */
    private String generateIdempotencyKey(String cartId) {
        try {
            long timestampBucket = System.currentTimeMillis() / 60000; // minute-level bucket
            String raw = cartId + ":" + timestampBucket;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash, 0, 16); // 32-char hex string
        } catch (Exception e) {
            // Fallback to simple key
            return cartId + "-" + System.currentTimeMillis();
        }
    }
}
