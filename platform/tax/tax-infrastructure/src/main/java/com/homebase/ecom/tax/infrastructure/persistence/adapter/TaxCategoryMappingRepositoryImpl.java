package com.homebase.ecom.tax.infrastructure.persistence.adapter;

import com.homebase.ecom.tax.model.TaxCategoryMapping;
import com.homebase.ecom.tax.model.port.TaxCategoryMappingRepository;
import com.homebase.ecom.tax.infrastructure.persistence.mapper.TaxMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaxCategoryMappingRepositoryImpl implements TaxCategoryMappingRepository {
    private final TaxCategoryMappingJpaRepository jpa;
    private final TaxMapper mapper;
    public TaxCategoryMappingRepositoryImpl(TaxCategoryMappingJpaRepository jpa, TaxMapper mapper) { this.jpa = jpa; this.mapper = mapper; }

    @Override public Optional<TaxCategoryMapping> findByCategoryId(String categoryId) { return jpa.findByProductCategory(categoryId).map(mapper::toModel); }
    @Override public Optional<TaxCategoryMapping> findByCategory(String productCategory) { return jpa.findByProductCategory(productCategory).map(mapper::toModel); }
    @Override public Optional<TaxCategoryMapping> findByCategoryAndSubCategory(String productCategory, String subCategory) { return jpa.findByProductCategoryAndSubCategory(productCategory, subCategory).map(mapper::toModel); }
    @Override public Optional<TaxCategoryMapping> findByHsnCode(String hsnCode) { return jpa.findByHsnCode(hsnCode).map(mapper::toModel); }
    @Override public List<TaxCategoryMapping> findAll() { return jpa.findAll().stream().map(mapper::toModel).collect(Collectors.toList()); }
    @Override public TaxCategoryMapping save(TaxCategoryMapping mapping) { return mapper.toModel(jpa.save(mapper.toEntity(mapping))); }
}
