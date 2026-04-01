package com.homebase.ecom.product.service.postSaveHooks;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.service.event.ProductEventHandler;
import com.homebase.ecom.shared.event.ProductDiscontinuedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post-save hook for the DISCONTINUED state.
 * Publishes a ProductDiscontinuedEvent to Kafka so the Catalog module
 * can permanently remove the product from listings.
 */
public class DISCONTINUEDProductPostSaveHook implements PostSaveHook<Product> {

    private static final Logger log = LoggerFactory.getLogger(DISCONTINUEDProductPostSaveHook.class);

    @Autowired
    private ProductEventHandler productEventHandler;

    @Override
    public void execute(State startState, State endState, Product product, TransientMap map) {
        log.info("DISCONTINUED PostSaveHook triggered for product '{}' (id={})", product.getName(), product.getId());

        String reason = map.get("discontinueReason") != null
                ? map.get("discontinueReason").toString()
                : "Product end-of-life";

        ProductDiscontinuedEvent event = new ProductDiscontinuedEvent(
                product.getId(),
                reason);
        productEventHandler.publishProductDiscontinued(event);

        log.info("ProductDiscontinuedEvent scheduled for product '{}'. Reason: {}", product.getName(), reason);
    }
}
