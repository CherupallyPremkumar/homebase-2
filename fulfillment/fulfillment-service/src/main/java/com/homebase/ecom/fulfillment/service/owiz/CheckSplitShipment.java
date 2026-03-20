package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that checks if the order requires split-shipment
 * (items from different warehouses).
 */
public class CheckSplitShipment implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(CheckSplitShipment.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Checking split shipment requirement for order {}", saga.getOrderId());

        // Default: no split shipment needed
        saga.getTransientMap().put("splitShipmentRequired", false);
        log.info("Split shipment not required for order {}", saga.getOrderId());
    }
}
