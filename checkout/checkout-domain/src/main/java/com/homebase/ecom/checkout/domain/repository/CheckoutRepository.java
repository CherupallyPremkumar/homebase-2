package com.homebase.ecom.checkout.domain.repository;

import com.homebase.ecom.checkout.domain.model.Checkout;
import java.util.Optional;
import java.util.UUID;

public interface CheckoutRepository {
    Optional<Checkout> findById(UUID id);
    Optional<Checkout> findByIdempotencyKey(String idempotencyKey);
    void save(Checkout checkout);
}
