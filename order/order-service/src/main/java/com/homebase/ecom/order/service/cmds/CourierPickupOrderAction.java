package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.CourierPickupOrderPayload;
import com.homebase.ecom.order.service.validator.OrderPolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * STM Action: Record courier pickup for shipment.
 * Transitions the order from PICKED to SHIPPED.
 * Stores shipping details and sets estimated delivery date.
 */
public class CourierPickupOrderAction extends AbstractSTMTransitionAction<Order, CourierPickupOrderPayload> {

    @Autowired
    private OrderPolicyValidator policyValidator;

    @Override
    public void transitionTo(Order order,
            CourierPickupOrderPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // 1. Store shipping details in transientMap
        if (payload.getCarrier() != null) {
            order.getTransientMap().put("carrier", payload.getCarrier());
        }
        if (payload.getTrackingNumber() != null) {
            order.getTransientMap().put("trackingNumber", payload.getTrackingNumber());
        }
        order.getTransientMap().put("shippedAt", LocalDateTime.now().toString());

        // 2. Set estimated delivery date from payload or from policy defaults
        int deliveryDays;
        if (payload.getEstimatedDeliveryDays() != null) {
            deliveryDays = payload.getEstimatedDeliveryDays();
        } else {
            deliveryDays = policyValidator.getMaxFulfillmentDays();
        }
        LocalDateTime estimatedDelivery = LocalDateTime.now().plusDays(deliveryDays);
        order.getTransientMap().put("estimatedDeliveryDate", estimatedDelivery.toString());

        order.getTransientMap().previousPayload = payload;
    }
}
