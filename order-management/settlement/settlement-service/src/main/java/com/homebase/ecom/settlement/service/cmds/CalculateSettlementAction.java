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
 *
 * Money amounts are in smallest currency unit (paise for INR).
 * All intermediate calculations use BigDecimal for precision, then convert back to paise.
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

        // Order amount in paise — convert to BigDecimal for calculation
        long orderAmountPaise = settlement.getOrderAmount() != null
                ? settlement.getOrderAmount().getAmount()
                : 0L;
        BigDecimal orderAmount = BigDecimal.valueOf(orderAmountPaise);

        // Calculate amounts in paise
        long commissionPaise = orderAmount.multiply(commissionRate)
                .setScale(0, RoundingMode.HALF_UP).longValue();
        long platformFeePaise = orderAmount.multiply(platformFeeRate)
                .setScale(0, RoundingMode.HALF_UP).longValue();
        long netPaise = orderAmountPaise - commissionPaise - platformFeePaise;

        // Apply any existing adjustments (adjustment amounts stored as BigDecimal in SettlementAdjustment)
        long totalAdjustmentPaise = settlement.getTotalAdjustmentAmountPaise();
        if (totalAdjustmentPaise != 0) {
            netPaise += totalAdjustmentPaise;
        }

        settlement.setCommissionAmount(Money.of(commissionPaise, currency));
        settlement.setPlatformFee(Money.of(platformFeePaise, currency));
        settlement.setNetAmount(Money.of(netPaise, currency));

        // Validate minimum settlement amount (compare in paise)
        policyValidator.validateMinSettlementAmountPaise(netPaise);

        log.info("Settlement {} calculated: orderAmount={} paise, commission={} paise, platformFee={} paise, netAmount={} paise",
                settlement.getId(), orderAmountPaise, commissionPaise, platformFeePaise, netPaise);

        settlement.getTransientMap().previousPayload = payload;
    }
}
