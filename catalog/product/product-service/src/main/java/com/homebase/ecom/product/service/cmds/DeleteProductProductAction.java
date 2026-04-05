package com.homebase.ecom.product.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.DeleteProductProductPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action: Supplier or admin deletes a draft product.
 * Transitions from DRAFT to DISCONTINUED (soft delete).
 */
public class DeleteProductProductAction extends AbstractSTMTransitionAction<Product, DeleteProductProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(DeleteProductProductAction.class);

    @Override
    public void transitionTo(Product product,
            DeleteProductProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Deleting draft product '{}' (id={})", product.getName(), product.getId());

        product.getTransientMap().put("previousPayload", payload);
        String reason = payload.getComment() != null ? payload.getComment() : "Deleted by user";
        product.getTransientMap().put("discontinueReason", reason);

        log.info("Draft product '{}' marked for deletion", product.getName());
    }
}
