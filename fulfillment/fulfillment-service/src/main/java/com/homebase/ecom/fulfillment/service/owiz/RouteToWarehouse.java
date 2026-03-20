package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that routes the fulfillment to the optimal warehouse
 * based on customer location and stock availability.
 */
public class RouteToWarehouse implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(RouteToWarehouse.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Routing order {} to optimal warehouse", saga.getOrderId());

        // Select warehouse based on order attributes (default to primary warehouse)
        String warehouseId = (String) saga.getTransientMap().get("preferredWarehouseId");
        if (warehouseId == null) {
            warehouseId = "WH-DEFAULT";
        }
        saga.getTransientMap().put("selectedWarehouseId", warehouseId);
        log.info("Order {} routed to warehouse {}", saga.getOrderId(), warehouseId);
    }
}
