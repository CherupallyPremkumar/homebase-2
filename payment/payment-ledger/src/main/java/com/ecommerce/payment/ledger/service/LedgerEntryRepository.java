package com.ecommerce.payment.repository;

import com.ecommerce.payment.domain.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, String> {

    List<LedgerEntry> findByTransactionId(String transactionId);

    List<LedgerEntry> findByLedgerAccountId(String ledgerAccountId);
}
