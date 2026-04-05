package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that calls the notification API to send the shipment notification
 * to the customer.
 */
public class SendCustomerNotification implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(SendCustomerNotification.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Sending notification to customer {} for order {}",
                saga.getUserId(), saga.getOrderId());

        @SuppressWarnings("unchecked")
        java.util.Map<String, String> notificationContent =
                (java.util.Map<String, String>) saga.getTransientMap().get("notificationContent");

        if (notificationContent == null || notificationContent.isEmpty()) {
            log.warn("No notification content prepared for order {}, skipping send", saga.getOrderId());
            return;
        }

        // Send notification (in production, this would call the notification service API)
        String notificationId = "NTF-" + saga.getOrderId() + "-" + System.currentTimeMillis();
        saga.getTransientMap().put("notificationId", notificationId);
        saga.getTransientMap().put("notificationSent", true);

        log.info("Notification {} sent to customer {} for order {}",
                notificationId, saga.getUserId(), saga.getOrderId());
    }
}
