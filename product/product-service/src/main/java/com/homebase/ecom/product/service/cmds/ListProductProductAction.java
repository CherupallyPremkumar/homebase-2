package com.homebase.ecom.product.service.cmds;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.ListProductProductPayload;
import com.homebase.ecom.product.service.event.ProductEventPublisher;
import com.homebase.ecom.shared.event.ProductPublishedEvent;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Handles the listProduct transition (AT_WAREHOUSE → PUBLISHED).
 * Publishes a ProductPublishedEvent to Kafka so the Catalog module
 * can create a CatalogItem for browsing.
 */
public class ListProductProductAction extends AbstractSTMTransitionAction<Product, ListProductProductPayload> {

    @Autowired
    private ProductEventPublisher productEventPublisher;

    @Override
    public void transitionTo(Product product, ListProductProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        product.getTransientMap().put("previousPayload", payload);

        // Publish event so Catalog can create a CatalogItem
        ProductPublishedEvent event = new ProductPublishedEvent(product.getId());
        productEventPublisher.publishProductPublished(event);
    }
}
