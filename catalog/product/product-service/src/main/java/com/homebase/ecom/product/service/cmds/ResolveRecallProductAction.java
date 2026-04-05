package com.homebase.ecom.product.service.cmds;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.ResolveRecallProductPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action: Admin resolves a recall.
 * Product moves to DISCONTINUED — recalled products are never re-published.
 * Resolution summary documents the outcome.
 */
public class ResolveRecallProductAction extends AbstractSTMTransitionAction<Product, ResolveRecallProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(ResolveRecallProductAction.class);

    @Override
    public void transitionTo(Product product, ResolveRecallProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Resolving recall for product '{}' (id={})", product.getName(), product.getId());

        product.getTransientMap().put("previousPayload", payload);
        product.getTransientMap().put("recallResolutionSummary", payload.getResolutionSummary());

        log.info("Product '{}' recall resolved. Summary: {}. Moving to DISCONTINUED.",
                product.getName(), payload.getResolutionSummary());
    }
}
