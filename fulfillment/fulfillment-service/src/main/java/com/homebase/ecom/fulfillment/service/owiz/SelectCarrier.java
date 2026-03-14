package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that selects the appropriate shipping carrier
 * based on destination, weight, and shipping preferences.
 */
@Component("selectCarrier")
public class SelectCarrier implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(SelectCarrier.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Selecting carrier for order {}", saga.getOrderId());

        // Select carrier based on order characteristics
        // Default to standard carrier; can be overridden via transient map
        String preferredCarrier = (String) saga.getTransientMap().get("preferredCarrier");
        String selectedCarrier = (preferredCarrier != null) ? preferredCarrier : "STANDARD_CARRIER";

        saga.getTransientMap().put("selectedCarrier", selectedCarrier);
        log.info("Carrier '{}' selected for order {}", selectedCarrier, saga.getOrderId());
    }
}
