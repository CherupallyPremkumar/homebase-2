package com.homebase.ecom.product.service.cmds;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.RequestUpdateProductPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action: Supplier requests an update to a published product.
 * Stores the proposed changes snapshot in the product's transient map.
 * The product moves to PENDING_UPDATE where an admin must approve or reject.
 */
public class RequestUpdateProductAction extends AbstractSTMTransitionAction<Product, RequestUpdateProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(RequestUpdateProductAction.class);

    @Override
    public void transitionTo(Product product, RequestUpdateProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Supplier requesting update for published product '{}' (id={})", product.getName(), product.getId());

        product.getTransientMap().put("previousPayload", payload);
        // Store the pending changes for admin review
        if (payload.getPendingChanges() != null) {
            product.getTransientMap().put("pendingChanges", payload.getPendingChanges());
        }

        log.info("Product '{}' moved to PENDING_UPDATE for admin review", product.getName());
    }
}
