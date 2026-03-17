package com.homebase.ecom.tax.infrastructure.persistence.adapter;

import com.homebase.ecom.tax.infrastructure.persistence.entity.TaxCategoryMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxCategoryMappingJpaRepository extends JpaRepository<TaxCategoryMappingEntity, String> {

    Optional<TaxCategoryMappingEntity> findByCategoryId(String categoryId);

    Optional<TaxCategoryMappingEntity> findByHsnCode(String hsnCode);
}
