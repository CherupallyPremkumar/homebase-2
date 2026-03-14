package com.homebase.ecom.product.service.cmds;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.dto.MarkOutOfStockProductPayload;
import com.homebase.ecom.product.service.event.ProductEventPublisher;
import com.homebase.ecom.shared.event.ProductStockUpdatedEvent;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Handles the markOutOfStock transition (PUBLISHED → OUT_OF_STOCK).
 * Publishes a ProductStockUpdatedEvent so the Catalog module
 * can deactivate the CatalogItem.
 */
public class MarkOutOfStockProductAction extends AbstractSTMTransitionAction<Product, MarkOutOfStockProductPayload> {

    @Autowired
    private ProductEventPublisher productEventPublisher;

    @Override
    public void transitionTo(Product product, MarkOutOfStockProductPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        product.getTransientMap().put("previousPayload", payload);

        // Publish stock event so Catalog can hide the item
        ProductStockUpdatedEvent event = new ProductStockUpdatedEvent(
                product.getId(),
                0,
                ProductStockUpdatedEvent.StockStatus.OUT_OF_STOCK);
        productEventPublisher.publishStockUpdated(event);
    }
}
