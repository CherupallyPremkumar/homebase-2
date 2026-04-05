package com.homebase.ecom.product.service.cmds;

import com.homebase.ecom.product.service.validator.ProductPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.SubmitForReviewProductPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action: Supplier submits their product for admin review.
 * Validates that all required fields (name, description, category) are present
 * before allowing the DRAFT -> UNDER_REVIEW transition.
 */
public class SubmitForReviewProductAction extends AbstractSTMTransitionAction<Product, SubmitForReviewProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(SubmitForReviewProductAction.class);

    @Autowired
    private ProductPolicyValidator policyValidator;

    @Override
    public void transitionTo(Product product,
            SubmitForReviewProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Submitting product '{}' (id={}) for review", product.getName(), product.getId());

        // Validate all required fields: name, description, category
        policyValidator.validateSubmitForReview(product);

        product.getTransientMap().put("previousPayload", payload);
        log.info("Product '{}' submitted for review successfully", product.getName());
    }
}
