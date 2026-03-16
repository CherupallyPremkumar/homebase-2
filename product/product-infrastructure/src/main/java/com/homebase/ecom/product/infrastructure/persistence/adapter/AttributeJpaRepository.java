package com.homebase.ecom.product.infrastructure.persistence.adapter;

import com.homebase.ecom.product.infrastructure.persistence.entity.AttributeDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttributeJpaRepository extends JpaRepository<AttributeDefinitionEntity, String> {
    Optional<AttributeDefinitionEntity> findByCode(String code);
}
