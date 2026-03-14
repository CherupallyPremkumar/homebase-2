package com.homebase.ecom.cart.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.dto.AddDeliveryAddressPayload;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Action to handle adding delivery addresses to the cart.
 */
public class AddDeliveryAddressAction extends AbstractCartAction<AddDeliveryAddressPayload> {

    @Override
    public void transitionTo(Cart cart,
            AddDeliveryAddressPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        if (payload.getShippingAddress() != null) {
            cart.setShippingAddress(mapper.writeValueAsString(payload.getShippingAddress()));
        }
        if (payload.getBillingAddress() != null) {
            cart.setBillingAddress(mapper.writeValueAsString(payload.getBillingAddress()));
        }
    }
}
