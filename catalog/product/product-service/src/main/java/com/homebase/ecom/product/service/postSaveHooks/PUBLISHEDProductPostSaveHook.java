package com.homebase.ecom.product.service.postSaveHooks;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.service.event.ProductEventHandler;
import com.homebase.ecom.shared.event.ProductPublishedEvent;
import com.homebase.ecom.shared.event.ProductUpdatedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post-save hook for the PUBLISHED state.
 * Distinguishes between first publish and update approval:
 * - From UNDER_REVIEW: publishes ProductPublishedEvent (first time live)
 * - From PENDING_UPDATE: publishes ProductUpdatedEvent (approved changes)
 * - From DISABLED/ARCHIVED: publishes ProductPublishedEvent (re-activation)
 */
public class PUBLISHEDProductPostSaveHook implements PostSaveHook<Product> {

    private static final Logger log = LoggerFactory.getLogger(PUBLISHEDProductPostSaveHook.class);

    @Autowired
    private ProductEventHandler productEventHandler;

    @Override
    public void execute(State startState, State endState, Product product, TransientMap map) {
        log.info("PUBLISHED PostSaveHook triggered for product '{}' (id={}), from state: {}",
                product.getName(), product.getId(), startState);

        if (map.get("updateApproved") != null && Boolean.TRUE.equals(map.get("updateApproved"))) {
            // Approved update — catalog needs to refresh product data
            ProductUpdatedEvent event = new ProductUpdatedEvent(product.getId());
            productEventHandler.publishProductUpdated(event);
        } else {
            // First publish or re-activation — catalog creates/shows CatalogItem
            ProductPublishedEvent event = new ProductPublishedEvent(product.getId());
            productEventHandler.publishProductPublished(event);
        }
    }
}
