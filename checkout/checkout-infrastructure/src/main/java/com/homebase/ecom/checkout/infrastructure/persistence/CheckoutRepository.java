package com.homebase.ecom.checkout.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CheckoutRepository extends JpaRepository<CheckoutEntity, UUID> {
    Optional<CheckoutEntity> findByIdempotencyKey(String idempotencyKey);
}
