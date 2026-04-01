package com.homebase.ecom.product.infrastructure.persistence.adapter;

import com.homebase.ecom.product.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, String> {
    Optional<CategoryEntity> findBySlug(String slug);
    List<CategoryEntity> findByParentId(String parentId);
    List<CategoryEntity> findAllByActiveTrue();
}
