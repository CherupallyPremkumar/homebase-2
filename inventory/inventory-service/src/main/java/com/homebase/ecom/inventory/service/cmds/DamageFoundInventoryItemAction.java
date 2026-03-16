package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.DamageRecord;
import com.homebase.ecom.inventory.domain.model.DamageStatus;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.dto.DamageUnit;
import com.homebase.ecom.inventory.service.validator.InventoryItemPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.DamageFoundInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;

/**
 * STM action when damaged units are discovered during inbound QC inspection.
 * Records per-unit damage details for traceability.
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

        List<DamageRecord> unitDamages = buildDamageRecords(payload.getDamagedUnits(), damagedQty);
        inventory.recordDamage(damagedQty, unitDamages);

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

    static List<DamageRecord> buildDamageRecords(List<DamageUnit> damagedUnits, int damagedQty) {
        if (damagedUnits != null && !damagedUnits.isEmpty()) {
            return damagedUnits.stream()
                .map(u -> new DamageRecord(
                    u.getUnitIdentifier(), u.getLocation(), u.getDamageType(),
                    u.getDescription(), Instant.now(), DamageStatus.REPORTED))
                .toList();
        }
        return List.of();
    }
}
