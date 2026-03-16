package com.homebase.ecom.product.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.DisableProductProductPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action: Admin or system disables a published product.
 * Temporarily hides the product from the catalog (e.g., out of stock, policy violation).
 * Marks the product for a ProductDisabledEvent in the PostSaveHook.
 */
public class DisableProductProductAction extends AbstractSTMTransitionAction<Product, DisableProductProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(DisableProductProductAction.class);

    @Override
    public void transitionTo(Product product,
            DisableProductProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Disabling product '{}' (id={})", product.getName(), product.getId());

        product.getTransientMap().put("previousPayload", payload);
        product.getTransientMap().put("disableReason", payload.getReason());

        log.info("Product '{}' disabled. Reason: {}", product.getName(), payload.getReason());
    }
}
