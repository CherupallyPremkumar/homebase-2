package com.homebase.ecom.payment.ledger.service;

import com.homebase.ecom.payment.ledger.service.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, String> {

    List<LedgerEntry> findByTransactionId(String transactionId);

    List<LedgerEntry> findByLedgerAccountId(String ledgerAccountId);
}
