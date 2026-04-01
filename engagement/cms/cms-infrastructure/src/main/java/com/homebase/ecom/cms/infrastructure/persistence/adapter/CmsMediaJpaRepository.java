package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsMediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsMediaJpaRepository extends JpaRepository<CmsMediaEntity, String> {
    List<CmsMediaEntity> findByFolder(String folder);
}
