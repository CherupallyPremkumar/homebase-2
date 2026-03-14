package com.homebase.ecom.onboarding.infrastructure.persistence.adapter;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.homebase.ecom.onboarding.infrastructure.persistence.entity.OnboardingSagaEntity;

public interface OnboardingSagaJpaRepository extends JpaRepository<OnboardingSagaEntity, String> {
    Optional<OnboardingSagaEntity> findBySupplierId(String supplierId);
}
