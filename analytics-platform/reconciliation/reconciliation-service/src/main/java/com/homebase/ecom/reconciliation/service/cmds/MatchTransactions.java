package com.homebase.ecom.reconciliation.service.cmds;

import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Matches gateway transactions with system records by transaction ID and amount.
 * Sets the totalTransactions and matched counts on the result.
 */
public class MatchTransactions implements Command<ReconciliationContext> {

    private static final Logger logger = LoggerFactory.getLogger(MatchTransactions.class);

    @Override
    public void execute(ReconciliationContext context) throws Exception {
        Map<String, BigDecimal> gatewayTxns = context.getGatewayTransactions();
        Map<String, BigDecimal> systemTxns = context.getSystemTransactions();

        Set<String> allTransactionIds = new HashSet<>();
        allTransactionIds.addAll(gatewayTxns.keySet());
        allTransactionIds.addAll(systemTxns.keySet());

        int matched = 0;
        for (String txnId : allTransactionIds) {
            BigDecimal gatewayAmount = gatewayTxns.get(txnId);
            BigDecimal systemAmount = systemTxns.get(txnId);

            if (gatewayAmount != null && systemAmount != null
                    && gatewayAmount.compareTo(systemAmount) == 0) {
                matched++;
            }
        }

        context.getResult().setTotalTransactions(allTransactionIds.size());
        context.getResult().setMatched(matched);

        logger.info("Matched {} out of {} total transactions", matched, allTransactionIds.size());
    }
}
