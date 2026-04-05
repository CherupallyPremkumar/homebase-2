package com.homebase.ecom.product.service.cmds;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.UnarchiveProductProductPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action: Unarchive a product back to PUBLISHED.
 * Restores the product in the catalog. Marks for re-publish event.
 */
public class UnarchiveProductProductAction extends AbstractSTMTransitionAction<Product, UnarchiveProductProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(UnarchiveProductProductAction.class);

    @Override
    public void transitionTo(Product product, UnarchiveProductProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Unarchiving product '{}' (id={})", product.getName(), product.getId());

        product.getTransientMap().put("previousPayload", payload);
        product.getTransientMap().put("publishEvent", true);

        log.info("Product '{}' unarchived and will be re-published to catalog", product.getName());
    }
}
