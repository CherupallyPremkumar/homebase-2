package com.homebase.ecom.product.service.postSaveHooks;

import com.homebase.ecom.product.domain.model.Product;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for the UNDER_REVIEW state.
 * Logs when a product enters the review queue for admin attention.
 */
public class UNDER_REVIEWProductPostSaveHook implements PostSaveHook<Product> {

    private static final Logger log = LoggerFactory.getLogger(UNDER_REVIEWProductPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Product product, TransientMap map) {
        log.info("Product '{}' (id={}) is now UNDER_REVIEW and awaiting admin approval",
                product.getName(), product.getId());

        // In a full implementation, this would notify the admin queue
        // e.g., adminNotificationService.notifyNewProductForReview(product);
    }
}
