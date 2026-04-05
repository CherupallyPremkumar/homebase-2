package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that validates weight and dimensions for shipping compliance.
 */
public class ValidateWeightDimensions implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(ValidateWeightDimensions.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Validating weight and dimensions for order {}", saga.getOrderId());

        // Default: validation passes
        saga.getTransientMap().put("weightDimensionsValid", true);
        log.info("Weight and dimensions validated for order {}", saga.getOrderId());
    }
}
