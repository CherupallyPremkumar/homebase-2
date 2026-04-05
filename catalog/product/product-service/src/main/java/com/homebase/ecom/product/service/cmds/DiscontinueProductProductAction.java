package com.homebase.ecom.product.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.DiscontinueProductProductPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action: Admin or supplier permanently discontinues a product.
 * Removes the product from the catalog permanently.
 * Marks the product for a ProductDiscontinuedEvent in the PostSaveHook.
 */
public class DiscontinueProductProductAction extends AbstractSTMTransitionAction<Product, DiscontinueProductProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(DiscontinueProductProductAction.class);

    @Override
    public void transitionTo(Product product,
            DiscontinueProductProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Discontinuing product '{}' (id={})", product.getName(), product.getId());

        product.getTransientMap().put("previousPayload", payload);
        product.getTransientMap().put("discontinueReason", payload.getReason());

        log.info("Product '{}' discontinued permanently. Reason: {}", product.getName(), payload.getReason());
    }
}
