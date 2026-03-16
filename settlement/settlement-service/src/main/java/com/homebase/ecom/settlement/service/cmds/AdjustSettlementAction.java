package com.homebase.ecom.settlement.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.model.SettlementAdjustment;
import com.homebase.ecom.settlement.dto.AdjustSettlementPayload;
import com.homebase.ecom.settlement.service.validator.SettlementPolicyValidator;
import com.homebase.ecom.shared.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Handles the adjust event (dispute resolution with amount adjustment).
 * Validates adjustment limits and recalculates net amount.
 */
public class AdjustSettlementAction extends AbstractSTMTransitionAction<Settlement, AdjustSettlementPayload> {

    private static final Logger log = LoggerFactory.getLogger(AdjustSettlementAction.class);

    @Autowired
    private SettlementPolicyValidator policyValidator;

    @Override
    public void transitionTo(Settlement settlement, AdjustSettlementPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload == null || payload.getAdjustmentAmount() == null) {
            throw new IllegalArgumentException("Adjustment amount is required");
        }

        BigDecimal adjustmentAmount = payload.getAdjustmentAmount();
        String reason = payload.getAdjustmentReason() != null ? payload.getAdjustmentReason() : "Dispute adjustment";

        // Validate adjustment limit
        policyValidator.validateAdjustmentLimit(settlement, adjustmentAmount);

        // Record the adjustment
        SettlementAdjustment adjustment = new SettlementAdjustment(adjustmentAmount, reason, "FINANCE");
        settlement.getAdjustments().add(adjustment);

        // Recalculate net amount
        BigDecimal currentNet = settlement.getNetAmount() != null ? settlement.getNetAmount().getAmount() : BigDecimal.ZERO;
        BigDecimal newNet = currentNet.add(adjustmentAmount).setScale(2, RoundingMode.HALF_UP);
        String currency = settlement.getCurrency() != null ? settlement.getCurrency() : "INR";
        settlement.setNetAmount(new Money(newNet, currency));

        log.info("Settlement {} adjusted by {} for supplier {}. New net: {}. Reason: {}",
                settlement.getId(), adjustmentAmount, settlement.getSupplierId(), newNet, reason);

        settlement.getTransientMap().previousPayload = payload;
    }
}
