package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.shared.Money;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

/**
 * Refreshes cart pricing when an external price change is detected.
 *
 * Recalculates all prices via PricingPort. Compares old total with new total
 * and sets priceChanged flag so the UI can notify the customer.
 */
public class RefreshPricingCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        Money oldTotal = cart.getTotal();

        recalculatePricing(cart);

        Money newTotal = cart.getTotal();
        if (!oldTotal.equals(newTotal)) {
            cart.setPriceChanged(true);
            logActivity(cart, "refreshPricing", "Price changed from " + oldTotal + " to " + newTotal);
        } else {
            logActivity(cart, "refreshPricing", "Pricing refreshed, no change");
        }
    }
}
