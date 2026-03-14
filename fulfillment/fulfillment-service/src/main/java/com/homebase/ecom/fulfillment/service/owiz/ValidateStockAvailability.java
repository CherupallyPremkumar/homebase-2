package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

import java.util.List;
import java.util.Map;

/**
 * OWIZ command that validates stock availability for all order items
 * before proceeding with inventory reservation.
 */
@Component("validateStockAvailability")
public class ValidateStockAvailability implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(ValidateStockAvailability.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Validating stock availability for order {}", saga.getOrderId());

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> orderItems = (List<Map<String, Object>>)
                saga.getTransientMap().get("orderItems");

        if (orderItems == null || orderItems.isEmpty()) {
            log.warn("No order items found for order {}, skipping stock validation", saga.getOrderId());
            return;
        }

        for (Map<String, Object> item : orderItems) {
            String productId = (String) item.get("productId");
            int requestedQty = ((Number) item.get("quantity")).intValue();
            Integer availableStock = (Integer) item.getOrDefault("availableStock", requestedQty);

            if (availableStock < requestedQty) {
                String msg = String.format("Insufficient stock for product %s: requested=%d, available=%d",
                        productId, requestedQty, availableStock);
                saga.setErrorMessage(msg);
                throw new RuntimeException(msg);
            }
            log.debug("Stock check passed for product {}: requested={}, available={}",
                    productId, requestedQty, availableStock);
        }

        saga.getTransientMap().put("stockValidated", true);
        log.info("Stock validation passed for order {}", saga.getOrderId());
    }
}
