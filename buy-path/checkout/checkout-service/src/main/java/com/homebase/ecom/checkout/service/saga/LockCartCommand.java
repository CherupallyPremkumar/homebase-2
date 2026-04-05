package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.domain.port.CartLockPort;
import com.homebase.ecom.checkout.domain.port.CartLockPort.CartItemSnapshot;
import com.homebase.ecom.checkout.domain.port.CartLockPort.CartSnapshot;
import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.checkout.model.CheckoutItem;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.Command;
import org.chenile.workflow.service.stmcmds.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * OWIZ saga step 1: Lock the cart and snapshot its contents onto Checkout.
 * Uses CartLockPort (hexagonal) — adapter handles the actual Cart service call.
 */
public class LockCartCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(LockCartCommand.class);

    private final CartLockPort cartLockPort;

    public LockCartCommand(CartLockPort cartLockPort) {
        this.cartLockPort = cartLockPort;
    }

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();

        CartSnapshot snapshot = cartLockPort.lockCart(checkout.getCartId());

        checkout.setCustomerId(snapshot.customerId());
        String currency = snapshot.currency() != null ? snapshot.currency() : "INR";
        checkout.setSubtotal(Money.of(snapshot.subtotal(), currency));
        checkout.setDiscountAmount(Money.of(snapshot.discountAmount(), currency));
        checkout.setTotal(Money.of(snapshot.total(), currency));

        if (snapshot.couponCodes() != null) {
            checkout.setCouponCodes(new ArrayList<>(snapshot.couponCodes()));
        }

        if (snapshot.items() != null) {
            List<CheckoutItem> items = new ArrayList<>();
            for (CartItemSnapshot ci : snapshot.items()) {
                CheckoutItem item = new CheckoutItem();
                item.setProductId(ci.productId());
                item.setVariantId(ci.variantId());
                item.setSku(ci.sku());
                item.setProductName(ci.productName());
                item.setSupplierId(ci.supplierId());
                item.setQuantity(ci.quantity());
                item.setUnitPrice(Money.of(ci.unitPrice(), currency));
                item.setLineTotal(Money.of(ci.lineTotal(), currency));
                items.add(item);
            }
            checkout.setItems(items);
        }

        checkout.setLastCompletedStep("lockCart");
        log.info("[CHECKOUT SAGA] Cart {} locked, {} items snapshotted to checkout {}",
                checkout.getCartId(), checkout.getItems().size(), checkout.getId());
    }
}
