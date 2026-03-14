package com.homebase.ecom.product.service.cmds;

import com.homebase.ecom.product.service.validator.ProductPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.RejectProductProductPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action: Admin rejects a product after review.
 * Enforces that a rejection comment is mandatory to provide supplier feedback.
 * Stores the rejection reason in the transient map for the PostSaveHook to use.
 */
public class RejectProductProductAction extends AbstractSTMTransitionAction<Product, RejectProductProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(RejectProductProductAction.class);

    @Autowired
    private ProductPolicyValidator policyValidator;

    @Override
    public void transitionTo(Product product,
            RejectProductProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Admin rejecting product '{}' (id={})", product.getName(), product.getId());

        // Validate that rejection comment is provided
        policyValidator.validateRejectionComment(payload.getComment());

        product.getTransientMap().put("previousPayload", payload);
        product.getTransientMap().put("rejectionReason", payload.getComment());

        log.info("Product '{}' rejected with reason: {}", product.getName(), payload.getComment());
    }
}
