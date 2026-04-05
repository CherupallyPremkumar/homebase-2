package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsBlockJpaRepository extends JpaRepository<CmsBlockEntity, String> {
    List<CmsBlockEntity> findByPageIdOrderBySortOrderAsc(String pageId);
}
