package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that records that the notification was sent successfully.
 */
public class UpdateNotificationStatus implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(UpdateNotificationStatus.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Updating notification status for order {}", saga.getOrderId());

        Boolean notificationSent = (Boolean) saga.getTransientMap().get("notificationSent");
        if (Boolean.TRUE.equals(notificationSent)) {
            saga.getTransientMap().put("notificationTimestamp", java.time.Instant.now().toString());
            saga.getTransientMap().put("notificationStatus", "DELIVERED");
            log.info("Notification status recorded as DELIVERED for order {}", saga.getOrderId());
        } else {
            saga.getTransientMap().put("notificationStatus", "PENDING");
            log.warn("Notification not yet sent for order {}, status set to PENDING", saga.getOrderId());
        }
    }
}
