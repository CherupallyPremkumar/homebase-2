package com.homebase.ecom.catalog.infrastructure.persistence.repository;

import com.homebase.ecom.catalog.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CatalogCategoryJpaRepository extends JpaRepository<CategoryEntity, String> {
    @Query("SELECT c FROM CatalogCategoryEntity c WHERE c.parentId IS NULL AND c.active = true ORDER BY c.displayOrder")
    List<CategoryEntity> findRootCategories();

    @Query("SELECT c FROM CatalogCategoryEntity c WHERE c.parentId = :parentId AND c.active = true ORDER BY c.displayOrder")
    List<CategoryEntity> findByParentId(String parentId);

    List<CategoryEntity> findByActiveTrue();
}
