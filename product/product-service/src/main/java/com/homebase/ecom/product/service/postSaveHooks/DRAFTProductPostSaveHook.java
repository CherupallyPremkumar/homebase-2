package com.homebase.ecom.product.service.postSaveHooks;

import com.homebase.ecom.product.domain.model.Product;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for the DRAFT state.
 * When a product transitions back to DRAFT from UNDER_REVIEW (rejection),
 * logs the rejection reason for supplier notification.
 */
public class DRAFTProductPostSaveHook implements PostSaveHook<Product> {

    private static final Logger log = LoggerFactory.getLogger(DRAFTProductPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Product product, TransientMap map) {
        // Check if this is a rejection (transition from UNDER_REVIEW back to DRAFT)
        if (startState != null && "UNDER_REVIEW".equals(startState.getStateId())) {
            String reason = map.get("rejectionReason") != null
                    ? map.get("rejectionReason").toString()
                    : "No reason provided";

            log.info("Product '{}' (id={}) rejected and returned to DRAFT. Reason: {}",
                    product.getName(), product.getId(), reason);

            // In a full implementation, this would trigger a supplier notification
            // e.g., notificationService.notifySupplier(product.getSupplierId(), reason);
        }
    }
}
