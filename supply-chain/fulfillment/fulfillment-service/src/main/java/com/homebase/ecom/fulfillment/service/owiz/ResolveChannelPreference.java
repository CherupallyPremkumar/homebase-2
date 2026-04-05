package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that resolves the customer's notification channel preference
 * (email, SMS, push, WhatsApp).
 */
public class ResolveChannelPreference implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(ResolveChannelPreference.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Resolving channel preference for user {}", saga.getUserId());

        // Default to email channel
        String channel = (String) saga.getTransientMap().get("preferredChannel");
        if (channel == null) {
            channel = "EMAIL";
        }
        saga.getTransientMap().put("notificationChannel", channel);
        log.info("Notification channel '{}' resolved for user {}", channel, saga.getUserId());
    }
}
