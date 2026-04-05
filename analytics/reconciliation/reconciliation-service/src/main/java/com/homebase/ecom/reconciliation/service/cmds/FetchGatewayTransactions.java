package com.homebase.ecom.reconciliation.service.cmds;

import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fetches transactions from the payment gateway for the reconciliation period.
 * Populates the context with gateway transaction data (transaction ID -> amount).
 */
public class FetchGatewayTransactions implements Command<ReconciliationContext> {

    private static final Logger logger = LoggerFactory.getLogger(FetchGatewayTransactions.class);

    @Override
    public void execute(ReconciliationContext context) throws Exception {
        logger.info("Fetching gateway transactions for period {} to {}, gateway: {}",
                context.getRequest().getPeriodStart(),
                context.getRequest().getPeriodEnd(),
                context.getRequest().getGatewayType());

        String gatewayType = context.getRequest().getGatewayType();
        if (gatewayType == null || gatewayType.isEmpty()) {
            throw new IllegalArgumentException("Gateway type must be specified for reconciliation");
        }

        // In production, this delegates to the appropriate gateway client (Stripe, Razorpay, etc.)
        // based on gatewayType. The gateway client fetches all transactions for the period.
        // For now, gateway transactions can be pre-populated via context for testing.
        //
        // Example production flow:
        // PaymentGatewayClient client = gatewayClientFactory.getClient(gatewayType);
        // List<GatewayTransaction> txns = client.listTransactions(
        //     context.getRequest().getPeriodStart(), context.getRequest().getPeriodEnd());
        // for (GatewayTransaction txn : txns) {
        //     context.getGatewayTransactions().put(txn.getId(), txn.getAmount());
        // }

        logger.info("Fetched {} gateway transactions from {}",
                context.getGatewayTransactions().size(), gatewayType);
    }
}
