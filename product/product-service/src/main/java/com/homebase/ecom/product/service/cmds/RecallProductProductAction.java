package com.homebase.ecom.product.service.cmds;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.RecallProductProductPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action: Admin recalls a product for safety/compliance reasons.
 * This is a serious action — triggers customer notifications, blocks new orders,
 * and initiates return processing for affected orders.
 * Recall reason is mandatory.
 */
public class RecallProductProductAction extends AbstractSTMTransitionAction<Product, RecallProductProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(RecallProductProductAction.class);

    @Override
    public void transitionTo(Product product, RecallProductProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.warn("RECALL initiated for product '{}' (id={}) from state {}",
                product.getName(), product.getId(), startState);

        product.getTransientMap().put("previousPayload", payload);
        product.getTransientMap().put("recallReason", payload.getRecallReason());
        product.getTransientMap().put("recallReferenceNumber", payload.getRecallReferenceNumber());
        product.getTransientMap().put("recalledFromState", startState);

        log.warn("Product '{}' RECALLED. Reason: {}. Ref: {}",
                product.getName(), payload.getRecallReason(), payload.getRecallReferenceNumber());
    }
}
