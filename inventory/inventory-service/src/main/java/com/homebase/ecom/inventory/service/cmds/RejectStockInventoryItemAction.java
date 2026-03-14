package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.model.InventoryStatus;
import com.homebase.ecom.inventory.service.validator.InventoryItemPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.RejectStockInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action for formally rejecting incoming stock during inspection.
 * Requires a mandatory rejection comment for audit trail.
 */
public class RejectStockInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, RejectStockInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(RejectStockInventoryItemAction.class);

    @Autowired
    private InventoryItemPolicyValidator policyValidator;

    @Override
    public void transitionTo(InventoryItem inventory,
            RejectStockInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Rule enforcement: rejection must have an audit comment
        policyValidator.validateRejectionComment(payload.getComment());

        // Zero out quantities since stock is rejected
        inventory.setAvailableQuantity(0);
        inventory.setStatus(InventoryStatus.DISCONTINUED);

        log.info("Stock REJECTED for productId={}, reason={}", inventory.getProductId(), payload.getComment());

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("rejectionReason", payload.getComment());
    }
}
