package com.homebase.ecom.inventory.service.postSaveHooks;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.port.InventoryEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for PARTIAL_DAMAGE state.
 * Publishes DamageDetectedEvent to notify operations team.
 */
public class PARTIAL_DAMAGEInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(PARTIAL_DAMAGEInventoryItemPostSaveHook.class);

    private final InventoryEventPublisherPort eventPublisher;

    public PARTIAL_DAMAGEInventoryItemPostSaveHook(InventoryEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        Integer damagedQty = (Integer) map.get("damagedQuantity");
        Boolean severe = (Boolean) map.get("severeDamage");
        if (damagedQty != null) {
            eventPublisher.publishDamageDetected(inventory, damagedQty, Boolean.TRUE.equals(severe));
            log.info("Published DamageDetectedEvent for productId={}, damagedQty={}, severe={}",
                    inventory.getProductId(), damagedQty, severe);
        }
    }
}
