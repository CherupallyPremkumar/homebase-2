package com.homebase.ecom.reconciliation.service.cmds;

import com.homebase.ecom.reconciliation.dto.MismatchDto;
import com.homebase.ecom.reconciliation.dto.MismatchDto.MismatchType;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Attempts to auto-resolve minor mismatches that fall within a configurable tolerance threshold.
 * Amount mismatches within the tolerance (e.g., rounding differences) are auto-resolved.
 * Missing records cannot be auto-resolved and remain for manual review.
 */
public class AutoResolveCommand implements Command<ReconciliationContext> {

    private static final Logger logger = LoggerFactory.getLogger(AutoResolveCommand.class);

    /** Tolerance threshold for auto-resolving amount mismatches (e.g., rounding differences) */
    private static final BigDecimal TOLERANCE_THRESHOLD = new BigDecimal("0.50");

    @Override
    public void execute(ReconciliationContext context) throws Exception {
        List<MismatchDto> remaining = new ArrayList<>();
        int autoResolved = 0;

        for (MismatchDto mismatch : context.getDetectedMismatches()) {
            if (canAutoResolve(mismatch)) {
                autoResolved++;
                logger.debug("Auto-resolved mismatch for transaction {}: gateway={}, system={}",
                        mismatch.getTransactionId(),
                        mismatch.getGatewayAmount(),
                        mismatch.getSystemAmount());
            } else {
                remaining.add(mismatch);
            }
        }

        context.setDetectedMismatches(remaining);
        context.getResult().setAutoResolved(autoResolved);
        context.getResult().setPendingReview(remaining.size());

        logger.info("Auto-resolved {} mismatches, {} remain for manual review",
                autoResolved, remaining.size());
    }

    private boolean canAutoResolve(MismatchDto mismatch) {
        if (mismatch.getMismatchType() != MismatchType.AMOUNT_MISMATCH) {
            return false;
        }
        if (mismatch.getGatewayAmount() == null || mismatch.getSystemAmount() == null) {
            return false;
        }
        BigDecimal difference = mismatch.getGatewayAmount()
                .subtract(mismatch.getSystemAmount()).abs();
        return difference.compareTo(TOLERANCE_THRESHOLD) <= 0;
    }
}
