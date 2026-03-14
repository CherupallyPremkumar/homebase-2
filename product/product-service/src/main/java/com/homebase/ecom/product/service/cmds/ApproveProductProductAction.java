package com.homebase.ecom.product.service.cmds;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.ApproveProductProductPayload;
import com.homebase.ecom.product.service.validator.ProductPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action: Admin approves a product after review.
 * Validates publish-readiness (description, images) and marks
 * the product for event publishing in the PUBLISHED PostSaveHook.
 */
public class ApproveProductProductAction extends AbstractSTMTransitionAction<Product, ApproveProductProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(ApproveProductProductAction.class);

    @Autowired
    private ProductPolicyValidator policyValidator;

    @Override
    public void transitionTo(Product product, ApproveProductProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Admin approving product '{}' (id={})", product.getName(), product.getId());

        // Validate publish-readiness: description and images required
        policyValidator.validatePublish(product);

        product.getTransientMap().put("previousPayload", payload);
        // Mark for event publishing in PostSaveHook
        product.getTransientMap().put("publishEvent", true);

        log.info("Product '{}' approved and will be published to catalog", product.getName());
    }
}
