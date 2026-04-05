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
 *
 * Adjustment amounts from the payload are in paise (smallest currency unit).
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

        // Validate adjustment limit (compares paise values)
        policyValidator.validateAdjustmentLimit(settlement, adjustmentAmount);

        // Record the adjustment
        SettlementAdjustment adjustment = new SettlementAdjustment(adjustmentAmount, reason, "FINANCE");
        settlement.getAdjustments().add(adjustment);

        // Recalculate net amount in paise
        long currentNetPaise = settlement.getNetAmount() != null ? settlement.getNetAmount().getAmount() : 0L;
        long adjustmentPaise = adjustmentAmount.longValue();
        long newNetPaise = currentNetPaise + adjustmentPaise;
        String currency = settlement.getCurrency() != null ? settlement.getCurrency() : "INR";
        settlement.setNetAmount(Money.of(newNetPaise, currency));

        log.info("Settlement {} adjusted by {} paise for supplier {}. New net: {} paise. Reason: {}",
                settlement.getId(), adjustmentPaise, settlement.getSupplierId(), newNetPaise, reason);

        settlement.getTransientMap().previousPayload = payload;
    }
}
