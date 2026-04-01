package com.homebase.ecom.payment.ledger.service;

import com.homebase.ecom.payment.ledger.service.LedgerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount, String> {

    Optional<LedgerAccount> findByName(String name);
}
