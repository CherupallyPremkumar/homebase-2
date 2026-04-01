package com.homebase.ecom.product.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.EnableProductProductPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action: Admin re-enables a disabled product.
 * Restores the product to PUBLISHED state, making it visible in the catalog again.
 * Marks the product for a ProductPublishedEvent in the PostSaveHook.
 */
public class EnableProductProductAction extends AbstractSTMTransitionAction<Product, EnableProductProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(EnableProductProductAction.class);

    @Override
    public void transitionTo(Product product,
            EnableProductProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Re-enabling product '{}' (id={})", product.getName(), product.getId());

        product.getTransientMap().put("previousPayload", payload);
        // Mark for re-publish event in PostSaveHook
        product.getTransientMap().put("publishEvent", true);

        log.info("Product '{}' re-enabled and will be re-published to catalog", product.getName());
    }
}
