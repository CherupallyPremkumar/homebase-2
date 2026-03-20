package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that builds the notification content from order and shipment data.
 */
public class PrepareShipmentNotification implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(PrepareShipmentNotification.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Preparing shipment notification for order {}", saga.getOrderId());

        java.util.Map<String, String> notificationContent = new java.util.HashMap<>();
        notificationContent.put("recipientId", saga.getUserId());
        notificationContent.put("orderId", saga.getOrderId());
        notificationContent.put("shipmentId", saga.getShipmentId());
        notificationContent.put("trackingNumber", saga.getTrackingNumber());
        notificationContent.put("type", "SHIPMENT_CREATED");
        notificationContent.put("subject", "Your order " + saga.getOrderId() + " has been shipped!");
        notificationContent.put("message", "Your order has been shipped with tracking number: " + saga.getTrackingNumber());

        saga.getTransientMap().put("notificationContent", notificationContent);
        log.info("Shipment notification prepared for order {} to user {}", saga.getOrderId(), saga.getUserId());
    }
}
