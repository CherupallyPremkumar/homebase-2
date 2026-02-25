package com.ecommerce.payment.repository;

import com.ecommerce.payment.domain.ProviderObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderObjectRepository extends JpaRepository<ProviderObject, String> {

    Optional<ProviderObject> findByGatewayTypeAndObjectTypeAndProviderObjectId(
            String gatewayType,
            String objectType,
            String providerObjectId);
}
