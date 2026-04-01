package com.homebase.ecom.fulfillment.service.cmds;

import org.chenile.core.context.ChenileExchange;
import org.chenile.owiz.OrchExecutor;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * STM transition action that creates a shipment for the order.
 * Delegates to the create-shipment-chain OWIZ flow for sub-step orchestration.
 */
public class CreateShipmentAction extends AbstractSTMTransitionAction<FulfillmentSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(CreateShipmentAction.class);

    @Autowired
    @Qualifier("fulfillmentOwizExecutor")
    private OrchExecutor<ChenileExchange> fulfillmentOwizExecutor;

    @Override
    public void transitionTo(FulfillmentSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm,
                             Transition transition) throws Exception {
        log.info("Creating shipment for order {}", saga.getOrderId());

        ChenileExchange exchange = new ChenileExchange();
        exchange.setBody(saga);
        fulfillmentOwizExecutor.execute("create-shipment-chain", exchange);

        log.info("Shipment created for order {}", saga.getOrderId());
    }
}
