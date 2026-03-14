package com.homebase.ecom.product.service.postSaveHooks;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.service.event.ProductEventPublisher;
import com.homebase.ecom.shared.event.ProductDisabledEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post-save hook for the DISABLED state.
 * Publishes a ProductDisabledEvent to Kafka so the Catalog module
 * can hide the product from storefront listings.
 */
public class DISABLEDProductPostSaveHook implements PostSaveHook<Product> {

    private static final Logger log = LoggerFactory.getLogger(DISABLEDProductPostSaveHook.class);

    @Autowired
    private ProductEventPublisher productEventPublisher;

    @Override
    public void execute(State startState, State endState, Product product, TransientMap map) {
        log.info("DISABLED PostSaveHook triggered for product '{}' (id={})", product.getName(), product.getId());

        String reason = map.get("disableReason") != null
                ? map.get("disableReason").toString()
                : "No reason provided";

        ProductDisabledEvent event = new ProductDisabledEvent(
                product.getId(),
                reason);
        productEventPublisher.publishProductDisabled(event);

        log.info("ProductDisabledEvent scheduled for product '{}'. Reason: {}", product.getName(), reason);
    }
}
