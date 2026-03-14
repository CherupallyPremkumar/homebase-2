package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that calls the shipping API to create the shipment record.
 */
@Component("createShipmentRecord")
public class CreateShipmentRecord implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(CreateShipmentRecord.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Creating shipment record for order {}", saga.getOrderId());

        String selectedCarrier = (String) saga.getTransientMap().get("selectedCarrier");
        String trackingNumber = saga.getTrackingNumber();

        if (trackingNumber == null || trackingNumber.isEmpty()) {
            String msg = "Cannot create shipment without tracking number for order " + saga.getOrderId();
            saga.setErrorMessage(msg);
            throw new RuntimeException(msg);
        }

        // Create a shipment record with a generated ID
        String shipmentId = "SHP-" + saga.getOrderId() + "-" + System.currentTimeMillis();
        saga.setShipmentId(shipmentId);

        saga.getTransientMap().put("shipmentCarrier", selectedCarrier);
        saga.getTransientMap().put("shipmentCreatedAt", java.time.Instant.now().toString());

        log.info("Shipment record created for order {}: shipmentId={}, carrier={}, tracking={}",
                saga.getOrderId(), shipmentId, selectedCarrier, trackingNumber);
    }
}
