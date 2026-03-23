package com.homebase.ecom.cart.infrastructure.persistence.repository;

import com.homebase.ecom.cart.infrastructure.persistence.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartJpaRepository extends JpaRepository<CartEntity, String> {
    Optional<CartEntity> findByCustomerIdAndState_StateId(String customerId, String stateId);
}
