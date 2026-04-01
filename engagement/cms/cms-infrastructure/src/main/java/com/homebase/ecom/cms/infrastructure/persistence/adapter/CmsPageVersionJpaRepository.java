package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsPageVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsPageVersionJpaRepository extends JpaRepository<CmsPageVersionEntity, String> {
    List<CmsPageVersionEntity> findByPageIdOrderByVersionNumberDesc(String pageId);
}
