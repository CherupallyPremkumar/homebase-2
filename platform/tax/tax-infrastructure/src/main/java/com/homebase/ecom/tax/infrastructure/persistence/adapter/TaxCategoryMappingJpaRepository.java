package com.homebase.ecom.tax.infrastructure.persistence.adapter;

import com.homebase.ecom.tax.infrastructure.persistence.entity.TaxCategoryMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaxCategoryMappingJpaRepository extends JpaRepository<TaxCategoryMappingEntity, String> {
    Optional<TaxCategoryMappingEntity> findByProductCategory(String productCategory);
    Optional<TaxCategoryMappingEntity> findByProductCategoryAndSubCategory(String productCategory, String subCategory);
    Optional<TaxCategoryMappingEntity> findByHsnCode(String hsnCode);
}
