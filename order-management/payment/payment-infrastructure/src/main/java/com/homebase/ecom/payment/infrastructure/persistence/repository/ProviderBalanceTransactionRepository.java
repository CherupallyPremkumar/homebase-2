package com.homebase.ecom.payment.infrastructure.persistence.repository;

import com.homebase.ecom.payment.infrastructure.persistence.entity.ProviderBalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderBalanceTransactionRepository extends JpaRepository<ProviderBalanceTransaction, String> {

    Optional<ProviderBalanceTransaction> findByGatewayTypeAndProviderBalanceTxnId(
            String gatewayType,
            String providerBalanceTxnId);

    boolean existsByGatewayTypeAndProviderBalanceTxnId(String gatewayType, String providerBalanceTxnId);

    List<ProviderBalanceTransaction> findByGatewayTypeAndProviderPayoutIdOrderByOccurredAtAsc(
            String gatewayType,
            String providerPayoutId);
}
