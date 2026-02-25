package com.ecommerce.payment.repository;

import com.ecommerce.payment.domain.Payout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayoutRepository extends JpaRepository<Payout, String> {

    Optional<Payout> findByGatewayTypeAndProviderPayoutId(String gatewayType, String providerPayoutId);

    List<Payout> findByGatewayTypeOrderByPayoutAtDesc(String gatewayType);
}
