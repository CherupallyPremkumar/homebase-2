package com.homebase.ecom.product.service.postSaveHooks;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.service.event.ProductEventHandler;
import com.homebase.ecom.shared.event.ProductRecalledEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post-save hook for RECALLED state.
 * Publishes ProductRecalledEvent — triggers cross-service actions:
 * catalog removal, order blocking, fulfillment halt, inventory quarantine, customer notifications.
 */
public class RECALLEDProductPostSaveHook implements PostSaveHook<Product> {

    private static final Logger log = LoggerFactory.getLogger(RECALLEDProductPostSaveHook.class);

    @Autowired
    private ProductEventHandler productEventHandler;

    @Override
    public void execute(State startState, State endState, Product product, TransientMap map) {
        String reason = map.get("recallReason") != null ? map.get("recallReason").toString() : "Unknown";
        String refNumber = map.get("recallReferenceNumber") != null ? map.get("recallReferenceNumber").toString() : "N/A";

        log.warn("RECALLED PostSaveHook triggered for product '{}' (id={}). Reason: {}. Ref: {}",
                product.getName(), product.getId(), reason, refNumber);

        ProductRecalledEvent event = new ProductRecalledEvent(product.getId(), reason, refNumber);
        productEventHandler.publishProductRecalled(event);
    }
}
