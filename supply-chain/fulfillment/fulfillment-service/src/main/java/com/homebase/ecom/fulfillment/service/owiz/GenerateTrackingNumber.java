package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that generates a tracking number for the shipment.
 */
public class GenerateTrackingNumber implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(GenerateTrackingNumber.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Generating tracking number for order {}", saga.getOrderId());

        String selectedCarrier = (String) saga.getTransientMap().get("selectedCarrier");
        if (selectedCarrier == null) {
            selectedCarrier = "DEFAULT";
        }

        // Generate a tracking number based on carrier prefix + order ID + timestamp
        String trackingNumber = "TRK-" + selectedCarrier.substring(0, Math.min(3, selectedCarrier.length())).toUpperCase()
                + "-" + saga.getOrderId() + "-" + System.currentTimeMillis();
        saga.setTrackingNumber(trackingNumber);

        log.info("Tracking number '{}' generated for order {} with carrier '{}'",
                trackingNumber, saga.getOrderId(), selectedCarrier);
    }
}
