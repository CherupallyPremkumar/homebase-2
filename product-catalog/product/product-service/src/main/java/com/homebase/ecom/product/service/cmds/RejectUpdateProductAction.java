package com.homebase.ecom.product.service.cmds;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.RejectUpdateProductPayload;
import com.homebase.ecom.product.service.validator.ProductPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action: Admin rejects the pending update.
 * Product returns to PUBLISHED with original data unchanged.
 * Requires a mandatory rejection comment.
 */
public class RejectUpdateProductAction extends AbstractSTMTransitionAction<Product, RejectUpdateProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(RejectUpdateProductAction.class);

    @Autowired
    private ProductPolicyValidator policyValidator;

    @Override
    public void transitionTo(Product product, RejectUpdateProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Admin rejecting update for product '{}' (id={})", product.getName(), product.getId());

        // Rejection comment is mandatory
        policyValidator.validateRejectionComment(payload.getComment());

        product.getTransientMap().put("previousPayload", payload);
        product.getTransientMap().put("updateRejectionReason", payload.getComment());

        log.info("Product '{}' update rejected. Reason: {}. Returning to PUBLISHED unchanged.", product.getName(), payload.getComment());
    }
}
