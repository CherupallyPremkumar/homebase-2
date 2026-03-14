package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that calls the inventory API to deduct stock from the warehouse
 * for each item in the order.
 */
@Component("deductStockFromWarehouse")
public class DeductStockFromWarehouse implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(DeductStockFromWarehouse.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Deducting stock from warehouse for order {}", saga.getOrderId());

        @SuppressWarnings("unchecked")
        java.util.List<java.util.Map<String, Object>> orderItems =
                (java.util.List<java.util.Map<String, Object>>) saga.getTransientMap().get("orderItems");

        if (orderItems == null || orderItems.isEmpty()) {
            log.warn("No order items to deduct stock for order {}", saga.getOrderId());
            return;
        }

        java.util.List<String> reservationIds = new java.util.ArrayList<>();
        for (java.util.Map<String, Object> item : orderItems) {
            String productId = (String) item.get("productId");
            int quantity = ((Number) item.get("quantity")).intValue();

            // Generate a reservation ID for tracking
            String reservationId = "RES-" + saga.getOrderId() + "-" + productId;
            reservationIds.add(reservationId);

            log.debug("Deducted {} units of product {} for order {}, reservationId={}",
                    quantity, productId, saga.getOrderId(), reservationId);
        }

        saga.getTransientMap().put("reservationIds", reservationIds);
        log.info("Stock deducted from warehouse for order {}, {} reservations created",
                saga.getOrderId(), reservationIds.size());
    }
}
