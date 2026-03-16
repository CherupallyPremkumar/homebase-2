package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.cart.client.CartManagerClient;
import com.homebase.ecom.cart.dto.CartDto;
import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.checkout.model.CheckoutItem;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.Command;
import org.chenile.workflow.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * OWIZ saga step 1: Lock the cart and snapshot its contents onto Checkout.
 * Uses cart-client's CartManagerClient (typed business delegate).
 */
public class LockCartCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(LockCartCommand.class);

    @Autowired(required = false)
    private CartManagerClient cartManagerClient;

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();
        if (cartManagerClient != null) {
            // Lock the cart (transitions cart to CHECKOUT_INITIATED)
            CartDto cart = cartManagerClient.initiateCheckout(checkout.getCartId());

            // Snapshot cart data onto checkout
            if (cart != null) {
                snapshotCartToCheckout(checkout, cart);
            }

            log.info("[CHECKOUT SAGA] Cart {} locked, {} items snapshotted to checkout {}",
                    checkout.getCartId(), checkout.getItems().size(), checkout.getId());
        }
        checkout.setLastCompletedStep("lockCart");
    }

    private void snapshotCartToCheckout(Checkout checkout, CartDto cart) {
        checkout.setCustomerId(cart.getCustomerId());
        String currency = cart.getCurrency() != null ? cart.getCurrency() : "INR";
        checkout.setSubtotal(Money.of(cart.getSubtotal(), currency));
        checkout.setDiscountAmount(Money.of(cart.getDiscountAmount(), currency));
        checkout.setTotal(Money.of(cart.getTotal(), currency));

        if (cart.getCouponCodes() != null) {
            checkout.setCouponCodes(new ArrayList<>(cart.getCouponCodes()));
        }

        if (cart.getItems() != null) {
            List<CheckoutItem> items = new ArrayList<>();
            for (var cartItem : cart.getItems()) {
                CheckoutItem item = new CheckoutItem();
                item.setProductId(cartItem.getProductId());
                item.setVariantId(cartItem.getVariantId());
                item.setSku(cartItem.getSku());
                item.setProductName(cartItem.getProductName());
                item.setSupplierId(cartItem.getSupplierId());
                item.setQuantity(cartItem.getQuantity());
                item.setUnitPrice(Money.of(cartItem.getUnitPrice(), currency));
                item.setLineTotal(Money.of(cartItem.getLineTotal(), currency));
                items.add(item);
            }
            checkout.setItems(items);
        }
    }
}
