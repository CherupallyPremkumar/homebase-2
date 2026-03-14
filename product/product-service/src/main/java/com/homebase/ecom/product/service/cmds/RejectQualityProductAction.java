package com.homebase.ecom.product.service.cmds;

import com.homebase.ecom.product.service.validator.ProductPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.RejectQualityProductPayload;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action: Hub Dad rejects a product on quality grounds.
 * Evaluates:
 * - rules.quality.autoRejectBelowScore (flags advisory on transient map if
 * score very low)
 * - policies.lifecycle.rejectionRequiresComment (mandatory audit comment)
 */
public class RejectQualityProductAction extends AbstractSTMTransitionAction<Product, RejectQualityProductPayload> {

    @Autowired
    private ProductPolicyValidator policyValidator;

    @Override
    public void transitionTo(Product product, RejectQualityProductPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // --- Policy Enforcement: rejection comment required ---
        policyValidator.validateRejectionComment(payload.getComment());

        // --- Rule: flag if quality score is at or below the auto-reject threshold ---
        if (payload.getQualityScore() != null) {
            int autoRejectThreshold = policyValidator.getAutoRejectBelowScore();
            if (payload.getQualityScore() <= autoRejectThreshold) {
                product.getTransientMap().put("autoRejectQuality", true);
                product.getTransientMap().put("qualityScore", payload.getQualityScore());
            }
        }

        product.getTransientMap().put("previousPayload", payload);
    }
}
