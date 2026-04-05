package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsPageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CmsPageJpaRepository extends JpaRepository<CmsPageEntity, String> {
    Optional<CmsPageEntity> findBySlug(String slug);
    List<CmsPageEntity> findAllByPublishedTrue();
}
