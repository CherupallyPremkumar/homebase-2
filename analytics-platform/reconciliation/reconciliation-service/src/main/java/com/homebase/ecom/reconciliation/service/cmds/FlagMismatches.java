package com.homebase.ecom.reconciliation.service.cmds;

import com.homebase.ecom.reconciliation.dto.MismatchDto;
import com.homebase.ecom.reconciliation.dto.MismatchDto.MismatchType;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Identifies and records mismatches between gateway and system transactions.
 * Detects amount differences, records missing in system, and records missing in gateway.
 */
public class FlagMismatches implements Command<ReconciliationContext> {

    private static final Logger logger = LoggerFactory.getLogger(FlagMismatches.class);

    @Override
    public void execute(ReconciliationContext context) throws Exception {
        Map<String, BigDecimal> gatewayTxns = context.getGatewayTransactions();
        Map<String, BigDecimal> systemTxns = context.getSystemTransactions();
        Map<String, String> txnToOrder = context.getTransactionToOrderMap();

        Set<String> allTransactionIds = new HashSet<>();
        allTransactionIds.addAll(gatewayTxns.keySet());
        allTransactionIds.addAll(systemTxns.keySet());

        for (String txnId : allTransactionIds) {
            BigDecimal gatewayAmount = gatewayTxns.get(txnId);
            BigDecimal systemAmount = systemTxns.get(txnId);
            String orderId = txnToOrder.getOrDefault(txnId, null);

            if (gatewayAmount == null) {
                // Transaction exists in system but not in gateway
                context.getDetectedMismatches().add(new MismatchDto(
                        txnId, orderId, null, systemAmount, MismatchType.MISSING_IN_GATEWAY));
            } else if (systemAmount == null) {
                // Transaction exists in gateway but not in system
                context.getDetectedMismatches().add(new MismatchDto(
                        txnId, orderId, gatewayAmount, null, MismatchType.MISSING_IN_SYSTEM));
            } else if (gatewayAmount.compareTo(systemAmount) != 0) {
                // Both exist but amounts differ
                context.getDetectedMismatches().add(new MismatchDto(
                        txnId, orderId, gatewayAmount, systemAmount, MismatchType.AMOUNT_MISMATCH));
            }
        }

        context.getResult().setMismatched(context.getDetectedMismatches().size());
        logger.info("Flagged {} mismatches", context.getDetectedMismatches().size());
    }
}
