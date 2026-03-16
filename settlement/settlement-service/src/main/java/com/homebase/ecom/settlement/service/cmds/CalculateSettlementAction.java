package com.homebase.ecom.settlement.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.dto.CalculateSettlementPayload;
import com.homebase.ecom.settlement.service.validator.SettlementPolicyValidator;
import com.homebase.ecom.shared.Money;
import com.homebase.ecom.shared.CurrencyResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Handles the calculate event.
 * Computes commission, platform fee, and net amount from order amount
 * using cconfig-driven rates.
 */
public class CalculateSettlementAction extends AbstractSTMTransitionAction<Settlement, CalculateSettlementPayload> {

    private static final Logger log = LoggerFactory.getLogger(CalculateSettlementAction.class);

    @Autowired
    private SettlementPolicyValidator policyValidator;

    @Autowired
    private CurrencyResolver currencyResolver;

    @Override
    public void transitionTo(Settlement settlement, CalculateSettlementPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Validate settlement has required fields
        if (settlement.getSupplierId() == null || settlement.getSupplierId().isBlank()) {
            throw new IllegalArgumentException("Settlement must have a supplierId before calculation");
        }

        String currency = settlement.getCurrency() != null ? settlement.getCurrency() : currencyResolver.resolve().code();
        settlement.setCurrency(currency);

        // Get rates from cconfig via validator
        BigDecimal commissionRate = policyValidator.getCommissionRatePercent()
                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal platformFeeRate = policyValidator.getPlatformFeePercent()
                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        BigDecimal orderAmount = settlement.getOrderAmount() != null
                ? settlement.getOrderAmount().getAmount()
                : BigDecimal.ZERO;

        // Calculate amounts
        BigDecimal commissionAmount = orderAmount.multiply(commissionRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal platformFee = orderAmount.multiply(platformFeeRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal netAmount = orderAmount.subtract(commissionAmount).subtract(platformFee).setScale(2, RoundingMode.HALF_UP);

        // Apply any existing adjustments
        BigDecimal totalAdjustments = settlement.getTotalAdjustmentAmount();
        if (totalAdjustments.compareTo(BigDecimal.ZERO) != 0) {
            netAmount = netAmount.add(totalAdjustments).setScale(2, RoundingMode.HALF_UP);
        }

        settlement.setCommissionAmount(new Money(commissionAmount, currency));
        settlement.setPlatformFee(new Money(platformFee, currency));
        settlement.setNetAmount(new Money(netAmount, currency));

        // Validate minimum settlement amount
        policyValidator.validateMinSettlementAmount(netAmount);

        log.info("Settlement {} calculated: orderAmount={}, commission={}, platformFee={}, netAmount={}",
                settlement.getId(), orderAmount, commissionAmount, platformFee, netAmount);

        settlement.getTransientMap().previousPayload = payload;
    }
}
