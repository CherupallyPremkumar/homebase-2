package com.homebase.ecom.payment.infrastructure.persistence.repository;

import com.homebase.ecom.payment.infrastructure.persistence.entity.ProviderObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderObjectRepository extends JpaRepository<ProviderObject, String> {

    Optional<ProviderObject> findByGatewayTypeAndObjectTypeAndProviderObjectId(
            String gatewayType,
            String objectType,
            String providerObjectId);
}
