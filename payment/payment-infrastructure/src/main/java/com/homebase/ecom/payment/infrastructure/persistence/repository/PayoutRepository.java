package com.homebase.ecom.payment.infrastructure.persistence.repository;

import com.homebase.ecom.payment.infrastructure.persistence.entity.Payout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayoutRepository extends JpaRepository<Payout, String> {

    Optional<Payout> findByGatewayTypeAndProviderPayoutId(String gatewayType, String providerPayoutId);

    List<Payout> findByGatewayTypeOrderByPayoutAtDesc(String gatewayType);
}
