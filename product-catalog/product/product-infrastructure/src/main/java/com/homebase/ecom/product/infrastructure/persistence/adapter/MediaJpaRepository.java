package com.homebase.ecom.product.infrastructure.persistence.adapter;

import com.homebase.ecom.product.infrastructure.persistence.entity.MediaAssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaJpaRepository extends JpaRepository<MediaAssetEntity, String> {
}
