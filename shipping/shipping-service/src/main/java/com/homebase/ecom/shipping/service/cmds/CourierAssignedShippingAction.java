package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.CourierAssignedShippingPayload;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Handles the courierAssigned transition: AWAITING_PICKUP -> PICKED_UP.
 * Assigns carrier, generates tracking number, and sets estimated delivery.
 */
public class CourierAssignedShippingAction extends AbstractSTMTransitionAction<Shipping,
        CourierAssignedShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            CourierAssignedShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Set carrier from payload or default
        String carrier = payload.getCarrier();
        if (carrier != null && !carrier.isEmpty()) {
            shipping.setCarrier(carrier);
        } else if (shipping.getCarrier() == null || shipping.getCarrier().isEmpty()) {
            shipping.setCarrier("HOMEBASE-LOGISTICS");
        }

        // Generate tracking number if not provided in payload
        String trackingNumber = payload.getTrackingNumber();
        if (trackingNumber != null && !trackingNumber.isEmpty()) {
            shipping.setTrackingNumber(trackingNumber);
        } else if (shipping.getTrackingNumber() == null || shipping.getTrackingNumber().isEmpty()) {
            shipping.setTrackingNumber("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        // Build tracking URL based on carrier
        shipping.setTrackingUrl(buildTrackingUrl(shipping.getCarrier(), shipping.getTrackingNumber()));

        // Set shipped timestamp
        shipping.setShippedAt(new Date());

        // Calculate estimated delivery date
        int deliveryDays = payload.getEstimatedDeliveryDays();
        if (deliveryDays <= 0) {
            deliveryDays = 5;
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, deliveryDays);
        shipping.setEstimatedDelivery(cal.getTime());
    }

    private String buildTrackingUrl(String carrier, String trackingNumber) {
        if (carrier == null || trackingNumber == null) return null;
        switch (carrier.toUpperCase()) {
            case "DELHIVERY":
                return "https://www.delhivery.com/track/package/" + trackingNumber;
            case "DHL":
            case "DHL EXPRESS":
                return "https://www.dhl.com/track?trackingNumber=" + trackingNumber;
            case "BLUEDART":
                return "https://www.bluedart.com/tracking/" + trackingNumber;
            default:
                return "https://tracking.homebase.com/" + trackingNumber;
        }
    }
}
