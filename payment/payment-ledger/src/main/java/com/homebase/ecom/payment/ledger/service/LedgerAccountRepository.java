package com.homebase.ecom.payment.repository;

import com.homebase.ecom.payment.domain.LedgerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LedgerAccountRepository extends JpaRepository<LedgerAccount, String> {

    Optional<LedgerAccount> findByName(String name);
}
