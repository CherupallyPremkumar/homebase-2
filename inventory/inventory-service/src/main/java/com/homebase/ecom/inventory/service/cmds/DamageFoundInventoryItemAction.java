package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.service.validator.InventoryItemPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.DamageFoundInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action when damaged units are discovered during warehouse inspection.
 * Records the damage count, calculates damage percentage, and flags severe damage.
 */
public class DamageFoundInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, DamageFoundInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(DamageFoundInventoryItemAction.class);

    @Autowired
    private InventoryItemPolicyValidator policyValidator;

    @Override
    public void transitionTo(InventoryItem inventory,
            DamageFoundInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int damagedQty = payload.getDamagedQuantity() != null ? payload.getDamagedQuantity() : 0;

        // Record the damage in the domain model
        inventory.recordDamage(damagedQty);

        // Policy check: severe damage threshold
        boolean severeDamage = policyValidator.isSevereDamage(inventory, damagedQty);
        if (severeDamage) {
            log.warn("SEVERE DAMAGE detected for productId={}. {} of {} units damaged ({}%).",
                    inventory.getProductId(), damagedQty, inventory.getQuantity(), inventory.getDamagePercentage());
            inventory.getTransientMap().put("severeDamage", true);
        }

        log.info("Damage found for productId={}: {} units damaged out of {} total",
                inventory.getProductId(), damagedQty, inventory.getQuantity());

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("damagedQuantity", damagedQty);
        inventory.getTransientMap().put("damagePercentage", inventory.getDamagePercentage());
    }
}
