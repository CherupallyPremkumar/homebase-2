package com.homebase.ecom.reconciliation;

import com.homebase.ecom.reconciliation.dto.MismatchDto;
import com.homebase.ecom.reconciliation.dto.MismatchDto.MismatchType;
import com.homebase.ecom.reconciliation.dto.ReconciliationRequest;
import com.homebase.ecom.reconciliation.dto.ReconciliationResult;
import com.homebase.ecom.reconciliation.service.cmds.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration-style tests for the reconciliation pipeline, verifying end-to-end
 * behavior of the complete command chain.
 */
public class ReconciliationControllerTest {

    private MatchTransactions matchTransactions;
    private FlagMismatches flagMismatches;
    private AutoResolveCommand autoResolve;
    private GenerateReconciliationReport generateReport;

    @BeforeEach
    void setUp() {
        matchTransactions = new MatchTransactions();
        flagMismatches = new FlagMismatches();
        autoResolve = new AutoResolveCommand();
        generateReport = new GenerateReconciliationReport();
    }

    private ReconciliationResult runPipeline(ReconciliationContext context) throws Exception {
        matchTransactions.execute(context);
        flagMismatches.execute(context);
        autoResolve.execute(context);
        generateReport.execute(context);
        return context.getResult();
    }

    @Nested
    @DisplayName("Tolerance Boundary Tests")
    class ToleranceBoundaryTests {

        @Test
        @DisplayName("Exactly at tolerance threshold ($0.50) should be auto-resolved")
        void exactlyAtTolerance() throws Exception {
            ReconciliationContext context = buildContext();
            context.getGatewayTransactions().put("TXN-BOUNDARY", new BigDecimal("100.50"));
            context.getSystemTransactions().put("TXN-BOUNDARY", new BigDecimal("100.00"));
            context.getTransactionToOrderMap().put("TXN-BOUNDARY", "ORD-B");

            ReconciliationResult result = runPipeline(context);
            assertEquals(1, result.getAutoResolved(), "Exactly $0.50 diff should be auto-resolved");
            assertEquals(0, result.getPendingReview());
        }

        @Test
        @DisplayName("Just above tolerance ($0.51) should require manual review")
        void justAboveTolerance() throws Exception {
            ReconciliationContext context = buildContext();
            context.getGatewayTransactions().put("TXN-BOUNDARY", new BigDecimal("100.51"));
            context.getSystemTransactions().put("TXN-BOUNDARY", new BigDecimal("100.00"));
            context.getTransactionToOrderMap().put("TXN-BOUNDARY", "ORD-B");

            ReconciliationResult result = runPipeline(context);
            assertEquals(0, result.getAutoResolved(), "$0.51 diff should NOT be auto-resolved");
            assertEquals(1, result.getPendingReview());
        }
    }

    @Nested
    @DisplayName("Missing Transaction Tests")
    class MissingTransactionTests {

        @Test
        @DisplayName("Transaction in system but missing from gateway")
        void missingInGateway() throws Exception {
            ReconciliationContext context = buildContext();
            context.getSystemTransactions().put("TXN-SYS-ONLY", new BigDecimal("99.00"));
            context.getTransactionToOrderMap().put("TXN-SYS-ONLY", "ORD-SYS");

            ReconciliationResult result = runPipeline(context);
            assertEquals(1, result.getPendingReview());
            assertEquals(MismatchType.MISSING_IN_GATEWAY, result.getMismatches().get(0).getMismatchType());
        }

        @Test
        @DisplayName("Transaction in gateway but missing from system")
        void missingInSystem() throws Exception {
            ReconciliationContext context = buildContext();
            context.getGatewayTransactions().put("TXN-GW-ONLY", new BigDecimal("88.00"));

            ReconciliationResult result = runPipeline(context);
            assertEquals(1, result.getPendingReview());
            assertEquals(MismatchType.MISSING_IN_SYSTEM, result.getMismatches().get(0).getMismatchType());
        }

        @Test
        @DisplayName("Missing transactions cannot be auto-resolved")
        void missingCannotBeAutoResolved() throws Exception {
            ReconciliationContext context = buildContext();
            context.getGatewayTransactions().put("TXN-GW-ONLY", new BigDecimal("0.10"));
            context.getSystemTransactions().put("TXN-SYS-ONLY", new BigDecimal("0.10"));

            ReconciliationResult result = runPipeline(context);
            // Even with tiny amounts, missing records cannot be auto-resolved
            assertEquals(0, result.getAutoResolved());
            assertEquals(2, result.getPendingReview());
        }
    }

    private ReconciliationContext buildContext() {
        ReconciliationRequest request = new ReconciliationRequest(
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 14), "STRIPE");
        return new ReconciliationContext(request);
    }
}
