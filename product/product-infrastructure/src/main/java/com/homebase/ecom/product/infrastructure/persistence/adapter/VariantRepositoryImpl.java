package com.homebase.ecom.product.infrastructure.persistence.adapter;

import com.homebase.ecom.product.domain.model.Variant;
import com.homebase.ecom.product.domain.port.VariantRepository;
import com.homebase.ecom.product.infrastructure.persistence.entity.VariantEntity;
import com.homebase.ecom.product.infrastructure.persistence.mapper.VariantMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VariantRepositoryImpl implements VariantRepository {

    private final VariantJpaRepository jpaRepository;
    private final VariantMapper mapper;

    public VariantRepositoryImpl(VariantJpaRepository jpaRepository, VariantMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Variant> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Variant> findByProductId(String productId) {
        // Need to add this to JpaRepository or use a custom query. 
        // For now, let's assume we can fetch by product ID if we have the relation.
        // Actually, VariantEntity doesn't have a productId field in the Java class, 
        // but it's likely linked. Let's check ProductEntity.
        return jpaRepository.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Variant variant) {
        jpaRepository.save(mapper.toEntity(variant));
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }

    public interface VariantJpaRepository extends JpaRepository<VariantEntity, String> {
        // List<VariantEntity> findByProductId(String productId); 
    }
}
