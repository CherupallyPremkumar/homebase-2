package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.CreateLabelShippingPayload;
import com.homebase.ecom.shipping.service.validator.ShippingPolicyValidator;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Handles the createLabel transition: PENDING -> LABEL_CREATED.
 * Assigns carrier, generates tracking number, sets estimated delivery.
 */
public class CreateLabelShippingAction extends AbstractSTMTransitionAction<Shipping,
        CreateLabelShippingPayload> {

    @Autowired
    private ShippingPolicyValidator policyValidator;

    @Override
    public void transitionTo(Shipping shipping,
            CreateLabelShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Validate shipping method
        String method = payload.getShippingMethod();
        if (method != null && !method.isEmpty()) {
            policyValidator.validateShippingMethod(method);
            shipping.setShippingMethod(method);
        } else if (shipping.getShippingMethod() == null) {
            shipping.setShippingMethod("STANDARD");
        }

        // Set carrier from payload or default
        String carrier = payload.getCarrier();
        if (carrier != null && !carrier.isEmpty()) {
            shipping.setCarrier(carrier);
        } else if (shipping.getCarrier() == null || shipping.getCarrier().isEmpty()) {
            shipping.setCarrier("HOMEBASE-LOGISTICS");
        }

        // Generate tracking number if not provided
        String trackingNumber = payload.getTrackingNumber();
        if (trackingNumber != null && !trackingNumber.isEmpty()) {
            shipping.setTrackingNumber(trackingNumber);
        } else if (shipping.getTrackingNumber() == null || shipping.getTrackingNumber().isEmpty()) {
            shipping.setTrackingNumber("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        // Calculate estimated delivery date based on shipping method
        int deliveryDays = payload.getEstimatedDeliveryDays();
        if (deliveryDays <= 0) {
            deliveryDays = policyValidator.getDeliveryDaysForMethod(shipping.getShippingMethod());
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, deliveryDays);
        shipping.setEstimatedDeliveryDate(cal.getTime());

        // Set current location
        shipping.setCurrentLocation("Label created - warehouse");
    }
}
