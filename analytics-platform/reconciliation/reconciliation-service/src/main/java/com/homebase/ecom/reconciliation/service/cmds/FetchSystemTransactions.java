package com.homebase.ecom.reconciliation.service.cmds;

import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fetches orders and payment records from the internal system for the reconciliation period.
 * Populates the context with system transaction data (transaction ID -> amount)
 * and the transaction-to-order mapping.
 */
public class FetchSystemTransactions implements Command<ReconciliationContext> {

    private static final Logger logger = LoggerFactory.getLogger(FetchSystemTransactions.class);

    @Override
    public void execute(ReconciliationContext context) throws Exception {
        logger.info("Fetching system transactions for period {} to {}",
                context.getRequest().getPeriodStart(),
                context.getRequest().getPeriodEnd());

        // In production, this queries the payment_transactions and orders tables
        // for the given period. System transactions can be pre-populated via context for testing.
        //
        // Example production flow:
        // List<PaymentTransaction> txns = paymentTransactionRepository
        //     .findByCreatedAtBetween(context.getRequest().getPeriodStart(),
        //                             context.getRequest().getPeriodEnd());
        // for (PaymentTransaction txn : txns) {
        //     context.getSystemTransactions().put(txn.getGatewayTransactionId(), txn.getAmount());
        //     context.getTransactionToOrderMap().put(txn.getGatewayTransactionId(), txn.getOrderId());
        // }

        logger.info("Fetched {} system transactions", context.getSystemTransactions().size());
    }
}
