package com.homebase.ecom.product.service.cmds;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.ArchiveProductProductPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action: Archive a product.
 * Hides the product from catalog but preserves all data for order history,
 * returns, and analytics. Unlike DISCONTINUED, an archived product can be
 * re-enabled (unarchived) back to PUBLISHED.
 */
public class ArchiveProductProductAction extends AbstractSTMTransitionAction<Product, ArchiveProductProductPayload> {

    private static final Logger log = LoggerFactory.getLogger(ArchiveProductProductAction.class);

    @Override
    public void transitionTo(Product product, ArchiveProductProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Archiving product '{}' (id={})", product.getName(), product.getId());

        product.getTransientMap().put("previousPayload", payload);
        String reason = payload.getReason() != null ? payload.getReason() : "Product archived";
        product.getTransientMap().put("archiveReason", reason);

        log.info("Product '{}' archived. Reason: {}", product.getName(), reason);
    }
}
