package com.homebase.ecom.product.service.postSaveHooks;

import com.homebase.ecom.product.domain.model.Product;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for PENDING_UPDATE state.
 * Could notify admins that a product update is pending review.
 */
public class PENDING_UPDATEProductPostSaveHook implements PostSaveHook<Product> {

    private static final Logger log = LoggerFactory.getLogger(PENDING_UPDATEProductPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Product product, TransientMap map) {
        log.info("PENDING_UPDATE PostSaveHook triggered for product '{}' (id={}). Update awaiting admin review.",
                product.getName(), product.getId());
    }
}
