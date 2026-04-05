package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.UpdateAddressShippingPayload;

/**
 * Handles the updateAddress transition: IN_TRANSIT/DELIVERY_FAILED -> same state.
 * Customer or admin updates the delivery address mid-shipment.
 */
public class UpdateAddressShippingAction extends AbstractSTMTransitionAction<Shipping,
        UpdateAddressShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            UpdateAddressShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        String newAddress = payload.getNewAddress();
        if (newAddress != null && !newAddress.isEmpty()) {
            shipping.setToAddress(newAddress);
        }
    }
}
