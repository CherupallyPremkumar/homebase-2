package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that generates the pick list for warehouse staff.
 */
public class GeneratePickList implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(GeneratePickList.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Generating pick list for order {}", saga.getOrderId());

        String pickListId = "PL-" + saga.getOrderId() + "-" + System.currentTimeMillis();
        saga.getTransientMap().put("pickListId", pickListId);
        log.info("Pick list {} generated for order {}", pickListId, saga.getOrderId());
    }
}
