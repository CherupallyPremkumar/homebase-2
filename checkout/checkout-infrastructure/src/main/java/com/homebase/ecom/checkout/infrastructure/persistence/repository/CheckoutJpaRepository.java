package com.homebase.ecom.checkout.infrastructure.persistence.repository;

import com.homebase.ecom.checkout.infrastructure.persistence.entity.CheckoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutJpaRepository extends JpaRepository<CheckoutEntity, String> {
}
