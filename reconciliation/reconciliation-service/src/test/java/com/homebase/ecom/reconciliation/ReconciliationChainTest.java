package com.homebase.ecom.reconciliation;

import com.homebase.ecom.reconciliation.dto.MismatchDto;
import com.homebase.ecom.reconciliation.dto.MismatchDto.MismatchType;
import com.homebase.ecom.reconciliation.dto.ReconciliationRequest;
import com.homebase.ecom.reconciliation.dto.ReconciliationResult;
import com.homebase.ecom.reconciliation.service.cmds.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the reconciliation OWIZ command chain.
 * Tests the complete reconciliation flow: fetch, match, flag mismatches,
 * auto-resolve, and generate report.
 */
public class ReconciliationChainTest {

    private FetchGatewayTransactions fetchGateway;
    private FetchSystemTransactions fetchSystem;
    private MatchTransactions matchTransactions;
    private FlagMismatches flagMismatches;
    private AutoResolveCommand autoResolve;
    private GenerateReconciliationReport generateReport;

    @BeforeEach
    void setUp() {
        fetchGateway = new FetchGatewayTransactions();
        fetchSystem = new FetchSystemTransactions();
        matchTransactions = new MatchTransactions();
        flagMismatches = new FlagMismatches();
        autoResolve = new AutoResolveCommand();
        generateReport = new GenerateReconciliationReport();
    }

    // ==================================================================
    // Scenario: All transactions match
    // ==================================================================
    @Test
    void allTransactionsMatch() throws Exception {
        ReconciliationRequest request = new ReconciliationRequest(
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 14), "STRIPE");
        ReconciliationContext context = new ReconciliationContext(request);

        // Populate matching transactions
        context.getGatewayTransactions().put("TXN-001", new BigDecimal("100.00"));
        context.getGatewayTransactions().put("TXN-002", new BigDecimal("250.50"));
        context.getGatewayTransactions().put("TXN-003", new BigDecimal("75.00"));

        context.getSystemTransactions().put("TXN-001", new BigDecimal("100.00"));
        context.getSystemTransactions().put("TXN-002", new BigDecimal("250.50"));
        context.getSystemTransactions().put("TXN-003", new BigDecimal("75.00"));

        context.getTransactionToOrderMap().put("TXN-001", "ORD-001");
        context.getTransactionToOrderMap().put("TXN-002", "ORD-002");
        context.getTransactionToOrderMap().put("TXN-003", "ORD-003");

        // Execute chain
        fetchGateway.execute(context);
        fetchSystem.execute(context);
        matchTransactions.execute(context);
        flagMismatches.execute(context);
        autoResolve.execute(context);
        generateReport.execute(context);

        // Verify results
        ReconciliationResult result = context.getResult();
        assertEquals(3, result.getTotalTransactions());
        assertEquals(3, result.getMatched());
        assertEquals(0, result.getMismatched());
        assertEquals(0, result.getAutoResolved());
        assertEquals(0, result.getPendingReview());
        assertTrue(result.getMismatches().isEmpty());
    }

    // ==================================================================
    // Scenario: Amount mismatch detected
    // ==================================================================
    @Test
    void amountMismatchDetected() throws Exception {
        ReconciliationRequest request = new ReconciliationRequest(
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 14), "STRIPE");
        ReconciliationContext context = new ReconciliationContext(request);

        // TXN-001 matches, TXN-002 has a large amount mismatch
        context.getGatewayTransactions().put("TXN-001", new BigDecimal("100.00"));
        context.getGatewayTransactions().put("TXN-002", new BigDecimal("250.00"));

        context.getSystemTransactions().put("TXN-001", new BigDecimal("100.00"));
        context.getSystemTransactions().put("TXN-002", new BigDecimal("200.00"));

        context.getTransactionToOrderMap().put("TXN-001", "ORD-001");
        context.getTransactionToOrderMap().put("TXN-002", "ORD-002");

        // Execute chain
        matchTransactions.execute(context);
        flagMismatches.execute(context);
        autoResolve.execute(context);
        generateReport.execute(context);

        // Verify
        ReconciliationResult result = context.getResult();
        assertEquals(2, result.getTotalTransactions());
        assertEquals(1, result.getMatched());
        assertEquals(1, result.getMismatched());
        assertEquals(0, result.getAutoResolved());
        assertEquals(1, result.getPendingReview());
        assertEquals(1, result.getMismatches().size());

        MismatchDto mismatch = result.getMismatches().get(0);
        assertEquals("TXN-002", mismatch.getTransactionId());
        assertEquals(MismatchType.AMOUNT_MISMATCH, mismatch.getMismatchType());
        assertEquals(new BigDecimal("250.00"), mismatch.getGatewayAmount());
        assertEquals(new BigDecimal("200.00"), mismatch.getSystemAmount());
    }

    // ==================================================================
    // Scenario: Missing transaction in system
    // ==================================================================
    @Test
    void missingTransactionInSystem() throws Exception {
        ReconciliationRequest request = new ReconciliationRequest(
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 14), "STRIPE");
        ReconciliationContext context = new ReconciliationContext(request);

        // Gateway has TXN-001 and TXN-MISSING, system only has TXN-001
        context.getGatewayTransactions().put("TXN-001", new BigDecimal("100.00"));
        context.getGatewayTransactions().put("TXN-MISSING", new BigDecimal("300.00"));

        context.getSystemTransactions().put("TXN-001", new BigDecimal("100.00"));
        context.getTransactionToOrderMap().put("TXN-001", "ORD-001");

        // Execute chain
        matchTransactions.execute(context);
        flagMismatches.execute(context);
        autoResolve.execute(context);
        generateReport.execute(context);

        // Verify
        ReconciliationResult result = context.getResult();
        assertEquals(2, result.getTotalTransactions());
        assertEquals(1, result.getMatched());
        assertEquals(1, result.getMismatched());
        assertEquals(0, result.getAutoResolved());
        assertEquals(1, result.getPendingReview());

        MismatchDto mismatch = result.getMismatches().get(0);
        assertEquals("TXN-MISSING", mismatch.getTransactionId());
        assertEquals(MismatchType.MISSING_IN_SYSTEM, mismatch.getMismatchType());
        assertEquals(new BigDecimal("300.00"), mismatch.getGatewayAmount());
        assertNull(mismatch.getSystemAmount());
    }

    // ==================================================================
    // Scenario: Auto-resolve within tolerance
    // ==================================================================
    @Test
    void autoResolveWithinTolerance() throws Exception {
        ReconciliationRequest request = new ReconciliationRequest(
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 14), "STRIPE");
        ReconciliationContext context = new ReconciliationContext(request);

        // TXN-001 exact match, TXN-002 has $0.30 difference (within $0.50 tolerance)
        context.getGatewayTransactions().put("TXN-001", new BigDecimal("100.00"));
        context.getGatewayTransactions().put("TXN-002", new BigDecimal("250.30"));

        context.getSystemTransactions().put("TXN-001", new BigDecimal("100.00"));
        context.getSystemTransactions().put("TXN-002", new BigDecimal("250.00"));

        context.getTransactionToOrderMap().put("TXN-001", "ORD-001");
        context.getTransactionToOrderMap().put("TXN-002", "ORD-002");

        // Execute chain
        matchTransactions.execute(context);
        flagMismatches.execute(context);
        autoResolve.execute(context);
        generateReport.execute(context);

        // Verify
        ReconciliationResult result = context.getResult();
        assertEquals(2, result.getTotalTransactions());
        assertEquals(1, result.getMatched());
        // The $0.30 diff is within the $0.50 tolerance, so it gets auto-resolved
        assertEquals(1, result.getAutoResolved());
        assertEquals(0, result.getPendingReview());
        assertTrue(result.getMismatches().isEmpty());
    }

    // ==================================================================
    // Scenario: Manual review required for large mismatch
    // ==================================================================
    @Test
    void manualReviewRequiredForLargeMismatch() throws Exception {
        ReconciliationRequest request = new ReconciliationRequest(
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 14), "RAZORPAY");
        ReconciliationContext context = new ReconciliationContext(request);

        // TXN-001 matches, TXN-002 has $5.00 difference (exceeds $0.50 tolerance),
        // TXN-003 missing in gateway
        context.getGatewayTransactions().put("TXN-001", new BigDecimal("100.00"));
        context.getGatewayTransactions().put("TXN-002", new BigDecimal("255.00"));

        context.getSystemTransactions().put("TXN-001", new BigDecimal("100.00"));
        context.getSystemTransactions().put("TXN-002", new BigDecimal("250.00"));
        context.getSystemTransactions().put("TXN-003", new BigDecimal("80.00"));

        context.getTransactionToOrderMap().put("TXN-001", "ORD-001");
        context.getTransactionToOrderMap().put("TXN-002", "ORD-002");
        context.getTransactionToOrderMap().put("TXN-003", "ORD-003");

        // Execute chain
        matchTransactions.execute(context);
        flagMismatches.execute(context);
        autoResolve.execute(context);
        generateReport.execute(context);

        // Verify
        ReconciliationResult result = context.getResult();
        assertEquals(3, result.getTotalTransactions());
        assertEquals(1, result.getMatched());
        // 2 mismatches total: amount_mismatch ($5 diff, too big) + missing_in_gateway
        assertEquals(2, result.getMismatched());
        assertEquals(0, result.getAutoResolved());
        assertEquals(2, result.getPendingReview());
        assertEquals(2, result.getMismatches().size());

        // Verify both mismatches are present and need manual review
        boolean hasAmountMismatch = result.getMismatches().stream()
                .anyMatch(m -> m.getMismatchType() == MismatchType.AMOUNT_MISMATCH);
        boolean hasMissingInGateway = result.getMismatches().stream()
                .anyMatch(m -> m.getMismatchType() == MismatchType.MISSING_IN_GATEWAY);

        assertTrue(hasAmountMismatch, "Should have amount mismatch for TXN-002");
        assertTrue(hasMissingInGateway, "Should have missing-in-gateway for TXN-003");
    }

    // ==================================================================
    // Scenario: Mixed - some auto-resolved, some manual
    // ==================================================================
    @Test
    void mixedAutoResolvedAndManualReview() throws Exception {
        ReconciliationRequest request = new ReconciliationRequest(
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 14), "STRIPE");
        ReconciliationContext context = new ReconciliationContext(request);

        // TXN-001: exact match
        // TXN-002: $0.20 diff (auto-resolve)
        // TXN-003: $10.00 diff (manual review)
        // TXN-004: missing in system (manual review)
        context.getGatewayTransactions().put("TXN-001", new BigDecimal("100.00"));
        context.getGatewayTransactions().put("TXN-002", new BigDecimal("200.20"));
        context.getGatewayTransactions().put("TXN-003", new BigDecimal("310.00"));
        context.getGatewayTransactions().put("TXN-004", new BigDecimal("50.00"));

        context.getSystemTransactions().put("TXN-001", new BigDecimal("100.00"));
        context.getSystemTransactions().put("TXN-002", new BigDecimal("200.00"));
        context.getSystemTransactions().put("TXN-003", new BigDecimal("300.00"));

        context.getTransactionToOrderMap().put("TXN-001", "ORD-001");
        context.getTransactionToOrderMap().put("TXN-002", "ORD-002");
        context.getTransactionToOrderMap().put("TXN-003", "ORD-003");

        // Execute chain
        matchTransactions.execute(context);
        flagMismatches.execute(context);
        autoResolve.execute(context);
        generateReport.execute(context);

        // Verify
        ReconciliationResult result = context.getResult();
        assertEquals(4, result.getTotalTransactions());
        assertEquals(1, result.getMatched());
        assertEquals(1, result.getAutoResolved());
        assertEquals(2, result.getPendingReview());
        assertEquals(2, result.getMismatches().size());
    }

    // ==================================================================
    // Scenario: Empty transaction sets
    // ==================================================================
    @Test
    void emptyTransactionSets() throws Exception {
        ReconciliationRequest request = new ReconciliationRequest(
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 14), "STRIPE");
        ReconciliationContext context = new ReconciliationContext(request);

        // No transactions at all
        matchTransactions.execute(context);
        flagMismatches.execute(context);
        autoResolve.execute(context);
        generateReport.execute(context);

        ReconciliationResult result = context.getResult();
        assertEquals(0, result.getTotalTransactions());
        assertEquals(0, result.getMatched());
        assertEquals(0, result.getMismatched());
        assertEquals(0, result.getAutoResolved());
        assertEquals(0, result.getPendingReview());
    }

    // ==================================================================
    // Scenario: Gateway type validation
    // ==================================================================
    @Test
    void fetchGatewayTransactionsRequiresGatewayType() {
        ReconciliationRequest request = new ReconciliationRequest(
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 14), null);
        ReconciliationContext context = new ReconciliationContext(request);

        assertThrows(IllegalArgumentException.class, () -> fetchGateway.execute(context));
    }

    @Test
    void fetchGatewayTransactionsRejectsEmptyGatewayType() {
        ReconciliationRequest request = new ReconciliationRequest(
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 14), "");
        ReconciliationContext context = new ReconciliationContext(request);

        assertThrows(IllegalArgumentException.class, () -> fetchGateway.execute(context));
    }
}
