package com.homebase.ecom.catalog.infrastructure.persistence.repository;

import com.homebase.ecom.catalog.infrastructure.persistence.entity.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CollectionJpaRepository extends JpaRepository<CollectionEntity, String> {
    @Query("SELECT c FROM CollectionEntity c WHERE c.active = true AND c.autoUpdate = true")
    List<CollectionEntity> findAllActiveDynamicCollections();

    @Query("SELECT c FROM CollectionEntity c WHERE c.autoUpdate = true")
    List<CollectionEntity> findDynamicCollections();

    List<CollectionEntity> findByActiveTrue();

    int countByFeaturedTrue();
}
