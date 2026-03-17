package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.domain.port.PriceLockPort;
import com.homebase.ecom.checkout.domain.port.PriceLockPort.LockedPrice;
import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.Command;
import org.chenile.workflow.service.stmcmds.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OWIZ saga step 2: Lock prices for the checkout duration.
 * Uses PriceLockPort (hexagonal) — adapter calls Pricing service's lockPrice endpoint.
 */
public class LockPriceCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(LockPriceCommand.class);

    private final PriceLockPort priceLockPort;

    public LockPriceCommand(PriceLockPort priceLockPort) {
        this.priceLockPort = priceLockPort;
    }

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();

        String currency = checkout.getSubtotal() != null ? checkout.getSubtotal().getCurrency() : "INR";

        LockedPrice locked = priceLockPort.lockPrice(
                checkout.getCartId(),
                checkout.getCustomerId(),
                currency,
                checkout.getItems(),
                checkout.getCouponCodes()
        );

        checkout.setSubtotal(Money.of(locked.subtotal(), locked.currency()));
        checkout.setDiscountAmount(Money.of(locked.discountAmount(), locked.currency()));
        checkout.setTaxAmount(Money.of(locked.taxAmount(), locked.currency()));
        checkout.setShippingCost(Money.of(locked.shippingCost(), locked.currency()));
        checkout.setTotal(Money.of(locked.finalTotal(), locked.currency()));

        checkout.setLastCompletedStep("lockPrice");
        log.info("[CHECKOUT SAGA] Prices locked for checkout {}, total={}",
                checkout.getId(), checkout.getTotal());
    }
}
