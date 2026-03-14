package com.homebase.ecom.product.service.postSaveHooks;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.service.event.ProductEventPublisher;
import com.homebase.ecom.shared.event.ProductPublishedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post-save hook for the PUBLISHED state.
 * Publishes a ProductPublishedEvent to Kafka so the Catalog module
 * can create or re-activate the CatalogItem.
 */
public class PUBLISHEDProductPostSaveHook implements PostSaveHook<Product> {

    private static final Logger log = LoggerFactory.getLogger(PUBLISHEDProductPostSaveHook.class);

    @Autowired
    private ProductEventPublisher productEventPublisher;

    @Override
    public void execute(State startState, State endState, Product product, TransientMap map) {
        log.info("PUBLISHED PostSaveHook triggered for product '{}' (id={})", product.getName(), product.getId());

        ProductPublishedEvent event = new ProductPublishedEvent(
                product.getId());
        productEventPublisher.publishProductPublished(event);

        log.info("ProductPublishedEvent scheduled for product '{}'", product.getName());
    }
}
