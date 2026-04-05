package com.homebase.ecom.product.service.cmds;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.ApproveUpdateProductPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action: Admin approves the pending update.
 * The proposed changes go live — product returns to PUBLISHED with updated data.
 * Marks for re-publish event in PostSaveHook.
 */
public class ApproveUpdateProductAction extends AbstractSTMTransitionAction<Product, ApproveUpdateProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(ApproveUpdateProductAction.class);

    @Override
    public void transitionTo(Product product, ApproveUpdateProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Admin approving update for product '{}' (id={})", product.getName(), product.getId());

        product.getTransientMap().put("previousPayload", payload);
        // Mark for re-publish event — catalog needs to pick up the changes
        product.getTransientMap().put("publishEvent", true);
        product.getTransientMap().put("updateApproved", true);

        log.info("Product '{}' update approved, returning to PUBLISHED with changes live", product.getName());
    }
}
