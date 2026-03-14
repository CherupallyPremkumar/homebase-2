package com.homebase.ecom.reconciliation.service.cmds;

import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Final command in the chain. Generates the summary reconciliation report
 * by consolidating all findings into the ReconciliationResult.
 */
public class GenerateReconciliationReport implements Command<ReconciliationContext> {

    private static final Logger logger = LoggerFactory.getLogger(GenerateReconciliationReport.class);

    @Override
    public void execute(ReconciliationContext context) throws Exception {
        var result = context.getResult();

        // Attach remaining mismatches to the result
        result.setMismatches(context.getDetectedMismatches());

        // Recalculate mismatched count after auto-resolution
        result.setMismatched(result.getPendingReview() + result.getAutoResolved());

        logger.info("Reconciliation report generated: total={}, matched={}, mismatched={}, " +
                        "autoResolved={}, pendingReview={}",
                result.getTotalTransactions(),
                result.getMatched(),
                result.getMismatched(),
                result.getAutoResolved(),
                result.getPendingReview());
    }
}
