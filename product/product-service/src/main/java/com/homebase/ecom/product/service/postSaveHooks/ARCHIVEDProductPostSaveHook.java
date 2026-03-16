package com.homebase.ecom.product.service.postSaveHooks;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.service.event.ProductEventHandler;
import com.homebase.ecom.shared.event.ProductArchivedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post-save hook for ARCHIVED state.
 * Publishes ProductArchivedEvent — catalog hides it, data preserved for order history/returns.
 */
public class ARCHIVEDProductPostSaveHook implements PostSaveHook<Product> {

    private static final Logger log = LoggerFactory.getLogger(ARCHIVEDProductPostSaveHook.class);

    @Autowired
    private ProductEventHandler productEventHandler;

    @Override
    public void execute(State startState, State endState, Product product, TransientMap map) {
        log.info("ARCHIVED PostSaveHook triggered for product '{}' (id={})", product.getName(), product.getId());

        String reason = map.get("archiveReason") != null
                ? map.get("archiveReason").toString()
                : "Product archived";

        ProductArchivedEvent event = new ProductArchivedEvent(product.getId(), reason);
        productEventHandler.publishProductArchived(event);
    }
}
