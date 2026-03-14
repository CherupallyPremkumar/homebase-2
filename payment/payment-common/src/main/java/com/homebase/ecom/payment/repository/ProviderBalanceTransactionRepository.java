package com.homebase.ecom.payment.repository;

import com.homebase.ecom.payment.domain.ProviderBalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderBalanceTransactionRepository extends JpaRepository<ProviderBalanceTransaction, String> {

    Optional<ProviderBalanceTransaction> findByGatewayTypeAndProviderBalanceTxnId(
            String gatewayType,
            String providerBalanceTxnId);

    boolean existsByGatewayTypeAndProviderBalanceTxnId(String gatewayType, String providerBalanceTxnId);

    List<ProviderBalanceTransaction> findByGatewayTypeAndProviderPayoutIdOrderByOccurredAtAsc(
            String gatewayType,
            String providerPayoutId);
}
