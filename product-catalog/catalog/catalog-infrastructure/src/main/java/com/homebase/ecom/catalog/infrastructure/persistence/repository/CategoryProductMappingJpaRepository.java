package com.homebase.ecom.catalog.infrastructure.persistence.repository;

import com.homebase.ecom.catalog.infrastructure.persistence.entity.CategoryProductMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryProductMappingJpaRepository extends JpaRepository<CategoryProductMappingEntity, String> {
    List<CategoryProductMappingEntity> findByCategoryIdOrderByDisplayOrderAsc(String categoryId);
    Optional<CategoryProductMappingEntity> findByCategoryIdAndProductId(String categoryId, String productId);
    List<CategoryProductMappingEntity> findByProductId(String productId);
    void deleteByCategoryIdAndProductId(String categoryId, String productId);
}
