package com.homebase.ecom.catalog.infrastructure.persistence.repository;

import com.homebase.ecom.catalog.infrastructure.persistence.entity.CatalogItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CatalogItemJpaRepository extends JpaRepository<CatalogItemEntity, String> {
    Optional<CatalogItemEntity> findByProductId(String productId);
    int countByFeaturedTrue();
}
