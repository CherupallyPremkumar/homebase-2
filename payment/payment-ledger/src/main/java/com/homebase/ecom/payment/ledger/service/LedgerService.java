package com.homebase.ecom.payment.ledger.service;

import com.homebase.ecom.payment.domain.LedgerAccount;
import com.homebase.ecom.payment.domain.LedgerEntry;
import com.homebase.ecom.payment.repository.LedgerAccountRepository;
import com.homebase.ecom.payment.repository.LedgerEntryRepository;
import com.homebase.ecom.shared.Money;
import com.homebase.ecom.shared.CurrencyResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.chenile.cconfig.sdk.CconfigClient;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class LedgerService {

    private static final Logger log = LoggerFactory.getLogger(LedgerService.class);

    private final LedgerAccountRepository accountRepository;
    private final LedgerEntryRepository entryRepository;
    private final CurrencyResolver currencyResolver;

    public LedgerService(LedgerAccountRepository accountRepository,
            LedgerEntryRepository entryRepository,
            CurrencyResolver currencyResolver) {
        this.accountRepository = accountRepository;
        this.entryRepository = entryRepository;
        this.currencyResolver = currencyResolver;
    }

    /**
     * Records a balanced transaction in the ledger suite.
     * Ensures that Sum(Debits) + Sum(Credits) == 0.
     */
    @Transactional
    public void recordBalancedTransaction(String transactionId, List<EntryRequest> entries) {
        // Idempotency: if entries already exist for this transaction, do not re-apply
        // balances.
        if (!entryRepository.findByTransactionId(transactionId).isEmpty()) {
            log.info("Ledger entries already recorded for transactionId={}. Skipping.", transactionId);
            return;
        }

        BigDecimal balanceCheck = BigDecimal.ZERO;

        for (EntryRequest req : entries) {
            balanceCheck = balanceCheck.add(req.amount());
        }

        if (balanceCheck.compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException(
                    "Ledger transaction is unbalanced! Sum of entries is not exactly zero. Sum: " + balanceCheck);
        }

        for (EntryRequest req : entries) {
            LedgerAccount account = accountRepository.findByName(req.accountName())
                    .orElseThrow(() -> new IllegalArgumentException("Ledger Account not found: " + req.accountName()));

            // Update account running balance
            // For Assets/Expenses: Debits (positive) increase, Credits (negative) decrease
            // For Liability/Revenue/Equity: Debits (positive) decrease, Credits (negative)
            // increase
            account.setBalance(account.getBalance().add(req.amount()));
            accountRepository.save(account);

            // Record immutable entry
            LedgerEntry entry = new LedgerEntry();
            entry.setLedgerAccountId(account.getId());
            entry.setTransactionId(transactionId);

            String currency = currencyResolver.resolve().code();
            entry.setMoney(new Money(req.amount(), currency));
            entryRepository.save(entry);
        }

        log.info("Recorded balanced ledger transaction for ID: {}", transactionId);
    }

    /**
     * Helper record to pass to the ledger service.
     * Positive amount = Debit.
     * Negative amount = Credit.
     */
    public record EntryRequest(String accountName, BigDecimal amount) {
    }

    // Commonly Used Business Workflows

    /**
     * Records a standard successful charge based on amount and Stripe
     * flat/percentage fees.
     */
    @Transactional
    public void recordChargeSuccess(String transactionId, BigDecimal totalAmount, BigDecimal stripeFee) {
        // Phase 3 implementation
        List<EntryRequest> accurateEntries = Arrays.asList(
                new EntryRequest("Account_Receivable_Gateway", totalAmount), // Debit
                new EntryRequest("Sales_Revenue", totalAmount.negate()), // Credit
                new EntryRequest("Payment_Gateway_Fee_Exp", stripeFee), // Debit
                new EntryRequest("Fee_Payable_Stripe", stripeFee.negate()) // Credit
        );

        recordBalancedTransaction(transactionId, accurateEntries);
    }

    /**
     * Records a refund scenario.
     */
    @Transactional
    public void recordRefund(String refundId, BigDecimal refundedAmount) {
        List<EntryRequest> accurateEntries = Arrays.asList(
                new EntryRequest("Sales_Revenue", refundedAmount), // DEBIT (reducing revenue)
                new EntryRequest("Account_Receivable_Gateway", refundedAmount.negate()) // CREDIT (Stripe takes it from
                                                                                        // our payout)
        );

        recordBalancedTransaction(refundId, accurateEntries);
    }
}
