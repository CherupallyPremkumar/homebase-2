package com.homebase.ecom.tax.infrastructure.persistence.adapter;

import com.homebase.ecom.tax.infrastructure.persistence.mapper.TaxMapper;
import com.homebase.ecom.tax.model.TaxCategoryMapping;
import com.homebase.ecom.tax.model.port.TaxCategoryMappingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaxCategoryMappingRepositoryImpl implements TaxCategoryMappingRepository {

    private final TaxCategoryMappingJpaRepository jpaRepository;
    private final TaxMapper mapper;

    public TaxCategoryMappingRepositoryImpl(TaxCategoryMappingJpaRepository jpaRepository, TaxMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<TaxCategoryMapping> findByCategoryId(String categoryId) {
        return jpaRepository.findByCategoryId(categoryId).map(mapper::toModel);
    }

    @Override
    public Optional<TaxCategoryMapping> findByHsnCode(String hsnCode) {
        return jpaRepository.findByHsnCode(hsnCode).map(mapper::toModel);
    }

    @Override
    public List<TaxCategoryMapping> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toModel).collect(Collectors.toList());
    }

    @Override
    public TaxCategoryMapping save(TaxCategoryMapping mapping) {
        return mapper.toModel(jpaRepository.save(mapper.toEntity(mapping)));
    }
}
