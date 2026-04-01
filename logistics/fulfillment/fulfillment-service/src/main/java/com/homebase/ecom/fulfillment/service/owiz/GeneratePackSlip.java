package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that generates the pack slip with item details and shipping label.
 */
public class GeneratePackSlip implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(GeneratePackSlip.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Generating pack slip for order {}", saga.getOrderId());

        String packSlipId = "PS-" + saga.getOrderId() + "-" + System.currentTimeMillis();
        saga.getTransientMap().put("packSlipId", packSlipId);
        log.info("Pack slip {} generated for order {}", packSlipId, saga.getOrderId());
    }
}
