package com.homebase.ecom.cart.infrastructure.persistence.repository;

import com.homebase.ecom.cart.infrastructure.persistence.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartJpaRepository extends JpaRepository<CartEntity, String> {
}
