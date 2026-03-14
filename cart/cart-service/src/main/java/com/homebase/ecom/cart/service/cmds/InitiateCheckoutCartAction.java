package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.InitiateCheckoutCartPayload;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.shared.Money;
import java.math.BigDecimal;

import com.homebase.ecom.dto.OfferDto;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Contains customized logic for the transition. Common logic resides at
 * {@link DefaultSTMTransitionAction}
 * <p>
 * Use this class if you want to augment the common logic for this specific
 * transition
 * </p>
 * <p>
 * Use a customized payload if required instead of MinimalPayload
 * </p>
 */
public class InitiateCheckoutCartAction extends AbstractCartAction<InitiateCheckoutCartPayload> {

    @Override
    public void transitionTo(Cart cart,
            InitiateCheckoutCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        validateCustomerAccess();

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart");
        }

        BigDecimal calculatedTotal = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            OfferDto offer = validateAndGetOffer(item.getProductId(), item.getQuantity());

            BigDecimal currentItemTotal = offer.getPrice().getAmount().multiply(new BigDecimal(item.getQuantity()));
            if (currentItemTotal
                    .compareTo(item.getPrice().getAmount().multiply(new BigDecimal(item.getQuantity()))) != 0) {
                log.warn("Price changed for product {}. Old {}, New {}",
                        item.getProductId(), item.getPrice(), offer.getPrice());
                item.setPrice(offer.getPrice());
                currentItemTotal = offer.getPrice().getAmount().multiply(new BigDecimal(item.getQuantity()));
            }
            calculatedTotal = calculatedTotal.add(currentItemTotal);
        }

        String currency = (cart.getTotalAmount() != null) ? cart.getTotalAmount().getCurrency() : "INR";
        cart.setTotalAmount(new Money(calculatedTotal, currency));

        cart.getTransientMap().put("priceCalculationId", "LOCKED-HASH-" + System.currentTimeMillis());
        cart.getTransientMap().put("checkoutPayload", payload);

        logActivity(cart, "Checkout Initiated", "Cart locked for checkout and prices frozen.");
    }

}
