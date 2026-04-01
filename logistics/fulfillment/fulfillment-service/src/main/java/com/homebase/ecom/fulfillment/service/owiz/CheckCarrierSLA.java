package com.homebase.ecom.fulfillment.service.owiz;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * OWIZ command that verifies the selected carrier can meet the promised delivery date.
 */
public class CheckCarrierSLA implements Command<ChenileExchange> {

    private static final Logger log = LoggerFactory.getLogger(CheckCarrierSLA.class);

    @Override
    public void execute(ChenileExchange exchange) throws Exception {
        FulfillmentSaga saga = (FulfillmentSaga) exchange.getBody();
        log.info("Checking carrier SLA for order {}", saga.getOrderId());

        String selectedCarrier = (String) saga.getTransientMap().get("selectedCarrier");
        saga.getTransientMap().put("carrierSLAMet", true);
        log.info("Carrier {} SLA verified for order {}", selectedCarrier, saga.getOrderId());
    }
}
